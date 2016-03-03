/*--------------------------------------------------------------------------
 *  Copyright (c) 2009-2020, www.wuyushuo.com All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the wuyushuo developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: git_wuyu@163.com (tencent qq: 2094998377)
 *--------------------------------------------------------------------------
*/
package com.wuyu.plugin.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import com.squareup.okhttp.*;
import com.squareup.okhttp.Request.Builder;
import com.wuyu.plugin.agent.AgentFactory;
import com.wuyu.plugin.agent.AgentType;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class HttpKitUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HttpKitUtil.class);

    private static final String CHARSET = "UTF-8";

    static OkHttpClient client = new OkHttpClient();

    /**
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String get(String url, Map<String, String> headers, Map<String, String> params){
        return get(url, AgentFactory.getInstance().getUserAgent(AgentType.RANDOM), headers, params);
    }

    /**
     *
     * @param url
     * @param userAgent
     * @param headers
     * @param params
     * @return
     */
    public static String get(String url, String userAgent, Map<String, String> headers, Map<String, String> params){
        return get(url, userAgent, false, "localhost", 80, headers, null, params);
    }

    /**
     *
     * @param url
     * @param userAgent
     * @param isProxy
     * @param proxyHost
     * @param proxyPort
     * @param headers
     * @param params
     * @return
     */
    public static String get(String url, String userAgent, boolean isProxy, String proxyHost, int proxyPort,
                             Map<String, String> headers, Map<String, String> params){
        return get(url, userAgent, isProxy, proxyHost, proxyPort, headers, null, params);
    }

    /**
     *
     * @param url
     * @param userAgent
     * @param isProxy
     * @param proxyHost
     * @param proxyPort
     * @param headers
     * @param cookies
     * @param params
     * @return
     */
    public static String get(String url, String userAgent, boolean isProxy, String proxyHost, int proxyPort,
                             Map<String, String> headers, Map<String, String> cookies, Map<String, String> params){
        return get(url, userAgent, isProxy, proxyHost, proxyPort, headers, cookies, params, CHARSET);
    }

    /**
     *
     * @param url
     * @param userAgent
     * @param isProxy
     * @param proxyHost
     * @param proxyPort
     * @param headers
     * @param cookies
     * @param params
     * @param encoding
     * @return
     */
    public static String get(String url, String userAgent, boolean isProxy, String proxyHost, int proxyPort,
                             Map<String, String> headers, Map<String, String> cookies, Map<String, String> params, String encoding){
        try {
            OkHttpClient getClient = client.clone();
            getClient.setConnectTimeout(10, TimeUnit.SECONDS);
            getClient.setWriteTimeout(10, TimeUnit.SECONDS);
            getClient.setReadTimeout(30, TimeUnit.SECONDS);
            Builder builder = new Builder();
            if (isProxy) {
                getClient.setProxy(new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
            }
            if (userAgent != null) {
                builder.header("User-Agent", userAgent);
            }
            if (headers != null) {
                for (String key : headers.keySet()) {
                    builder.header(key, headers.get(key));
                }
            }
            String cookie = "";
            if (cookies != null) {
                StringBuffer buffer = new StringBuffer();
                for (String key : cookies.keySet()) {
                    buffer.append(key + "=" + cookies.get(key) + ",");
                }
                cookie = buffer.toString();
            }
            builder.header("cookie", cookie);

            if (params != null) {
                StringBuffer sb = new StringBuffer();
                if (params != null && params.size() > 0) {
                    for (String key : params.keySet()) {
                        sb.append(key + "=" + params.get(key) + "&");
                    }
                    url = url + "?" + sb.substring(0, sb.length() - 1);
                }
            }
            builder.url(url);
            Request httpRequest = builder.build();

            Response response = getClient.newCall(httpRequest).execute();
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                byte[] context = responseBody.bytes();
                return new String(context, encoding);
            }
        }catch (Exception e){
            LOG.error(e.getLocalizedMessage(), e);
        }
        return "";
    }

    /**
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String post(String url,Map<String, String> headers, Map<String, String> params){
        return post(url, AgentFactory.getInstance().getUserAgent(AgentType.RANDOM), headers, params);
    }

    /**
     *
     * @param url
     * @param userAgent
     * @param headers
     * @param params
     * @return
     */
    public static String post(String url, String userAgent, Map<String, String> headers, Map<String, String> params){
        return post(url, userAgent, false, "localhost", 80, headers, null, params);
    }


    /**
     *
     * @param url
     * @param userAgent
     * @param isProxy
     * @param proxyHost
     * @param proxyPort
     * @param headers
     * @param params
     * @return
     */
    public static String post(String url, String userAgent, boolean isProxy, String proxyHost, int proxyPort,
                              Map<String, String> headers, Map<String, String> params){
        return post(url, userAgent, isProxy, proxyHost, proxyPort, headers, null, params);
    }


    /**
     *
     * @param url
     * @param userAgent
     * @param isProxy
     * @param proxyHost
     * @param proxyPort
     * @param headers
     * @param cookies
     * @param params
     * @return
     */
    public static String post(String url, String userAgent, boolean isProxy, String proxyHost, int proxyPort,
                              Map<String, String> headers, Map<String, String> cookies, Map<String, String> params){
        return post(url, userAgent, isProxy, proxyHost, proxyPort, headers, cookies, params, CHARSET);
    }

    /**
     *
     * @param url
     * @param userAgent
     * @param isProxy
     * @param proxyHost
     * @param proxyPort
     * @param headers
     * @param cookies
     * @param params
     * @param encoding
     * @return
     */
    public static String post(String url, String userAgent, boolean isProxy, String proxyHost, int proxyPort,
                              Map<String, String> headers, Map<String, String> cookies, Map<String, String> params, String encoding){
        try {
            OkHttpClient postClient = client.clone();
            postClient.setConnectTimeout(10, TimeUnit.SECONDS);
            postClient.setWriteTimeout(10, TimeUnit.SECONDS);
            postClient.setReadTimeout(30, TimeUnit.SECONDS);
            Builder builder = new Builder();
            if (isProxy) {
                postClient.setProxy(new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
            }
            if (userAgent != null) {
                builder.header("User-Agent", userAgent);
            }
            if (headers != null) {
                for (String key : headers.keySet()) {
                    builder.header(key, headers.get(key));
                }
            }
            String cookie = "";
            if (cookies != null) {
                StringBuffer buffer = new StringBuffer();
                for (String key : cookies.keySet()) {
                    buffer.append(key + "=" + cookies.get(key) + ",");
                }
                cookie = buffer.toString();
            }
            builder.header("cookie", cookie);

            if (params != null) {
                if (params != null) {
                    FormEncodingBuilder formBuilder = new FormEncodingBuilder();
                    for (String key : params.keySet()) {
                        formBuilder.add(key, params.get(key));
                    }
                    RequestBody formBody = formBuilder.build();
                    builder.post(formBody);
                }
            }
            builder.url(url);
            Request httpRequest = builder.build();
            Response response = postClient.newCall(httpRequest).execute();

            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                byte[] context = responseBody.bytes();
                return new String(context, encoding);
            }
        }catch (Exception e){
            LOG.error(e.getLocalizedMessage(), e);
        }
        return "";
    }
}
