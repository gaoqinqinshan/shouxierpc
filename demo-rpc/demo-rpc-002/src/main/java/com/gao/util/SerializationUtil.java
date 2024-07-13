package com.gao.util;


import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.ObjenesisStd;
import org.objenesis.Objenesis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 序列化以及反序列化工具类
 */
public class SerializationUtil {
    // 创建HashMap存储序列化以及反序列化方法
    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap();

    private static Objenesis objenesis = new ObjenesisStd();

    private SerializationUtil() {

    }

    /**
     * 序列号（字节 -> 对象数组）
     */
    public static <T> byte[] serialize(T obj) {
        // 获取对象的类类型
        Class<T> cls = (Class<T>) obj.getClass();
        // 存储序列化过程的中间数据
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            // 获取对象结构
            Schema<T> schema = getSchema(cls);
            // 序列化
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            // 关闭资源
            buffer.clear();
        }
    }

    /**
     * 反序列化（对象数组 -> 字节）
     */
    public static <T> T deserialize(byte[] data, Class<T> cls) {
        try {
            // 创建对象数组
            T message = objenesis.newInstance(cls);
            Schema<T> schema = getSchema(cls);
            // 反序列化
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            // 返回
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }


    }

    /**
     * 获取对象结构放到缓存 没有就创建出来
     */
    private static <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            cachedSchema.put(cls, schema);
        }
        return null;
    }
}
