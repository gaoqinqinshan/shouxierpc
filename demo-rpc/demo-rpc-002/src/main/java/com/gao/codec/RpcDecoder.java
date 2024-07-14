package com.gao.codec;

import com.gao.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 * 解码器
 */
public class RpcDecoder extends ByteToMessageDecoder {
    // 保存在解码过程中的java对象
    private Class<?> genericClass;

    // 指定解码后的对象类型
    public RpcDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        // 如果缓冲区小于4 则返回
        if (in.readableBytes() < 4) {
            return;
        }
        // 标记当前的读指针位置
        in.markReaderIndex();
        int dataLength = in.readInt();
        // 如果缓冲区中的可读字节数少于数据长度，则重置读取位置并返回，等待更多数据到达
        if (in.readableBytes() < dataLength) {
            // 重置读索引
            in.resetReaderIndex();
            return;
        }
        // 创建字节数组
        byte[] data = new byte[dataLength];

        // 读取字节数据
        in.readBytes(data);
        // 将字节数组转换为对象
        out.add(SerializationUtil.deserialize(data, genericClass));
    }
}
