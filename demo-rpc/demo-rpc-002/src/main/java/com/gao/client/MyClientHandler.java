package com.gao.client;

import com.gao.future.SyncWriteFuture;
import com.gao.future.SyncWriteMap;
import com.gao.future.WriteFuture;
import com.gao.msg.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 客户端
 */
public class MyClientHandler extends ChannelInboundHandlerAdapter {

    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        // obj转换为Response
        Response msg = (Response) obj;
        // 获取请求id
        String requestId = msg.getRequestId();
        // 查找并设置响应
        WriteFuture future = (SyncWriteFuture) SyncWriteMap.syncKey.get(requestId);
        if (future != null) {
            future.setResponse(msg);
        }

    }

    // 异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
