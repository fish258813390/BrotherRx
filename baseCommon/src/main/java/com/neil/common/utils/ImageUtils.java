package com.neil.common.utils;

import android.text.TextUtils;

import java.io.File;

/**
 * 图片工具类
 * Created by neil on 2018/1/2 0002.
 */
public class ImageUtils {

    private static String BASE_PHOTO_URL = "";

    /**
     * @param url
     * @return
     */
    public static String getImageUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.contains("http") || new File(url).isFile()) {
                return url;
            } else {
                return BASE_PHOTO_URL + url;
            }
        } else {
            return "";
        }
    }
}
