package com.lhj.apigateway.filter;

import com.lhj.apicommon.entity.InterfaceInfo;
import com.lhj.apicommon.entity.User;
import com.lhj.apicommon.service.ApiBackendService;
import com.lhj.apicommon.service.InnerUserService;
import com.lhj.apicommon.vo.UserInterfaceInfoMessage;
import com.lhj.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.lhj.apicommon.constant.RabbitmqConstant.EXCHANGE_INTERFACE_CONSISTENT;
import static com.lhj.apicommon.constant.RabbitmqConstant.ROUTING_KEY_INTERFACE_CONSISTENT;

/**
 * @author lihua
 */
@Configuration
@Slf4j

public class interfaceFilter implements GlobalFilter, Ordered {
    final static List<String> WHITE_HOST = Arrays.asList(new String[]{"127.0.0.1", "47.120.32.55"});
    final static String INTERFACE_HOST="http://localhost:9091";

    @DubboReference
    private ApiBackendService apiBackendService;

    @DubboReference
    private InnerUserService innerUserService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //1.打上请求日志
        //2.黑白名单(可做可不做)
        //3.用户鉴权(API签名认证)
        //4.远程调用判断接口是否存在以及获取调用接口信息
        //5.判断接口是否还有调用次数，如果没有则直接拒绝
        //6.发起接口调用
        //7.获取响应结果，打上响应日志
        //8.接口调用次数+1
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String id = request.getId();
        log.info("请求id为:" + id);
        String path =INTERFACE_HOST+request.getPath();
        log.info("请求地址为:" + path);
        String method = request.getMethodValue();
        log.info("请求方法为:" + method);
        String localAddress = request.getLocalAddress().toString();
        log.info("请求localAddr为:" + localAddress);
        String remoteAddress = request.getRemoteAddress().toString();
        log.info("请求remoteAddr为:" + remoteAddress);
//        2.黑白名单(可做可不做)
//        if (!"127.0.0.1".equals(localAddress)){
//            response.setStatusCode(HttpStatus.FORBIDDEN);
//            return response.setComplete();
//        }

        //3.用户鉴权(API签名认证)
        HttpHeaders headers = request.getHeaders();

        String accessKey = headers.getFirst("accessKey");
        String body = headers.getFirst("body");
        String sign = headers.getFirst("sign");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");


        //根据accessKey获取secretKey，判断accessKey是否合法
        User invokeUser = null;
        try {
            invokeUser = apiBackendService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("远程调用获取调用接口用户的信息失败");
            e.printStackTrace();
        }

        if (invokeUser == null){
            return handleNoAuth(response);
        }
        String secretKey = invokeUser.getSecretKey();
        if (sign == null || !sign.equals(SignUtils.genSign(body,secretKey))){
            log.error("签名校验失败!!!!");
            return handleNoAuth(response);
        }

        //3.1防重放，使用redis存储请求的唯一标识，随机时间，并定时淘汰，那使用什么redis结构来实现嗯？
        //既然是单个数据，这样用string结构实现即可

//        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(nonce, "1", 5, TimeUnit.MINUTES);
        Boolean success=true;
        if (success ==false){
            log.error("随机数存储失败!!!!");
            return handleNoAuth(response);
        }

        //4.远程调用判断接口是否存在以及获取调用接口信息
        InterfaceInfo interFaceInfo = null;
        try {
//            http://localhost:9091/api/name/user
            interFaceInfo = apiBackendService.getInterFaceInfo(path, method);
        } catch (Exception e) {
            log.info("远程调用获取被调用接口信息失败");
            e.printStackTrace();
        }


        if (interFaceInfo == null){
            log.error("接口不存在！！！！");
            return handleNoAuth(response);
        }



        //5.判断接口是否还有调用次数，并且统计接口调用，将二者转化成原子性操作(backend本地服务的本地事务实现)，解决二者数据一致性问题
        boolean result=false;
        try {
            result = apiBackendService.invokeCount(invokeUser.getId(), interFaceInfo.getId());
        } catch (Exception e) {
            log.error("统计接口出现问题或者用户恶意调用不存在的接口");
            e.printStackTrace();
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return response.setComplete();
        }

        if (!result){
            log.error("接口剩余次数不足");
            return handleNoAuth(response);
        }

        //6.发起接口调用，网关路由实现
        Mono<Void> filter = chain.filter(exchange);

        return handleResponse(exchange,chain,interFaceInfo.getId(),invokeUser.getId(), result);


    }
    @NotNull
    private Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain,long interfaceInfoId,long userId,boolean result) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();

            //缓存数据
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            //响应状态码
            HttpStatus statusCode = originalResponse.getStatusCode();

            if(statusCode == HttpStatus.OK){
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存

                                //7.获取响应结果，打上响应日志
                                // 构建日志
                                log.info("接口调用响应状态码：" + originalResponse.getStatusCode());
                                //responseBody
                                String responseBody= new String(content, StandardCharsets.UTF_8);

                                //8.接口调用失败，利用消息队列实现接口统计数据的回滚；因为消息队列的可靠性所以我们选择消息队列而不是远程调用来实现
                                if (!(originalResponse.getStatusCode() == HttpStatus.OK)){
                                    log.error("接口异常调用-响应体:" + responseBody);
                                    UserInterfaceInfoMessage vo = new UserInterfaceInfoMessage(userId,interfaceInfoId);
                                    if(result==true)apiBackendService.recoverInterfaceLeft(interfaceInfoId,userId);
                                }

                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        }catch (Exception e){
            log.error("gateway log exception.\n" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}



