package com.gao.future;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 在多线程环境中安全地存储和访问写操作的状态
 */
public class SyncWriteMap {
    public static Map<String, WriteFuture> syncKey = new ConcurrentHashMap<String, WriteFuture>();
}