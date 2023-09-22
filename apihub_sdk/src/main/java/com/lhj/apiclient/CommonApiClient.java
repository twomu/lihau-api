package com.lhj.apiclient;

import cn.hutool.core.util.RandomUtil;

import java.util.HashMap;
import java.util.Map;

import static com.lhj.utils.SignUtils.genSign;

/**
 * @author lihua
 */
public  class CommonApiClient {

    protected String accessKey;

    protected String secretKey;

    public  CommonApiClient(){}
    public CommonApiClient(String accessKey, String secretKey) {
        this.accessKey=accessKey;
        this.secretKey=secretKey;
    }

    protected Map<String, String> getHeaderMap(String body) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        // 一定不能直接发送
//        hashMap.put("secretKey", secretKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("body", body);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", genSign(body, secretKey));
        return hashMap;
    }
}
