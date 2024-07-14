package com.gao.codec;

import com.gao.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

/**
 * 编码器
 */
public class RpcEncoder extends MessageToByteEncoder {
    private Class<?> genericClass;

    // 指定解码后的对象类型
    public RpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {
            // 序列化对象
            byte[] data = SerializationUtil.serialize(in);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
