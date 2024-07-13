package com.gao.server;

import com.gao.msg.Request;
import com.gao.msg.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 处理服务器端接收到的数据
 * 将 Request对象转换为Response对象，
 * 并将 Response 对象写入并刷新到通道。
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) {
        Request msg = (Request) obj;
        // 反馈
        Response request = new Response();
        // 设置请求id
        request.setRequestId(msg.getRequestId());
        request.setParam(msg.getResult() + "请求成功，反馈结果请接受处理！");
        // 把对象写进通道
        ctx.writeAndFlush(request);
        // 释放
        ReferenceCountUtil.release(msg);
    }


    //每当有数据从通道读取时，立即将数据刷新（发送）到网络中
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}


