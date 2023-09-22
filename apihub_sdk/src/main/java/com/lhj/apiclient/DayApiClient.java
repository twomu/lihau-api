package com.lhj.apiclient;

import cn.hutool.http.HttpRequest;

import static com.lhj.constant.Constant.GATEWAY_HOST;

/**
 * DayController-DayApiClient
 */
public class DayApiClient extends CommonApiClient{

    public DayApiClient(String accessKey, String secretKey) {
        super(accessKey, secretKey);
    }

    /**
     * 获取每日壁纸
     * @return
     */
    public String getDayWallpaperUrl(){
        return HttpRequest.post(GATEWAY_HOST+"/api/day/wallpaper")
                .addHeaders(getHeaderMap(""))
                .execute().body();
    }
}
