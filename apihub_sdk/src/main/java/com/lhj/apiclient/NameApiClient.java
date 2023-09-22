package com.lhj.apiclient;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.lhj.entity.User;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;

import static com.lhj.constant.Constant.GATEWAY_HOST;

/**
 * @author lihua
 */
public class NameApiClient extends CommonApiClient {


    public NameApiClient(){}
    public NameApiClient(String accessKey, String secretKey) {
        super(accessKey,secretKey);
    }

    public String getNameByGet(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.get(GATEWAY_HOST + "/api/name", paramMap);
        System.out.println(result);
        return result;
    }

    public User getUser(String userName) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("userName", userName);
        String result = HttpUtil.post(GATEWAY_HOST + "/api/name/user", paramMap);
        System.out.println(result);
        User user =(User) JSONUtil.parse(result);
        return user;
    }



    public String getNameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/name")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        System.out.println(httpResponse.getStatus());
        String result = httpResponse.body();
        System.out.println(result);
        return result;
    }
}
