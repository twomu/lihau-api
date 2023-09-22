package com.lhj.apiclient;

import cn.hutool.http.HttpRequest;

import static com.lhj.constant.Constant.GATEWAY_HOST;

/**
 * RandomController-RandomApiClient
 */
public class RandomApiClient extends CommonApiClient{

    public RandomApiClient(String accessKey, String secretKey) {
        super(accessKey, secretKey);
    }

    /**
     * 获取随机文本
     * @return
     */
    public String getRandomWork(){
        return HttpRequest.get(GATEWAY_HOST+"/api/random/word")
                .addHeaders(getHeaderMap(""))
                .execute().body();
    }

    /**
     * 获取随机动漫图片
     * @return
     */
    public String getRandomImageUrl(){
        return HttpRequest.post(GATEWAY_HOST+"/api/random/image")
                .addHeaders(getHeaderMap(""))
                .execute().body();
    }
}
