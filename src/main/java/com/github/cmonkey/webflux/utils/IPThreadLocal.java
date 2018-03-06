package com.github.cmonkey.webflux.utils;

public class IPThreadLocal {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setRemoteIp(String remoteIp){
        threadLocal.set(remoteIp);
    }

    public static String getRemoteIp(){
        return threadLocal.get();
    }
}
