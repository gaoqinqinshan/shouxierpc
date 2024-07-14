package com.gao.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 创建了一个基于Netty的服务器，
 * 监听端口 7397，并处理客户端的连接请求。
 * 使用自定义的解码器、编码器和处理器来处理 RPC（远程过程调用）请求和响应。
 */
public class ServerSocket implements Runnable {

    private ChannelFuture f;

    @Override
    public void run() {
        // 负责接受客户端的连接请求
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // 处理已接受的连接，进行读写操作。
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();


        try {
            // Netty启动
            ServerBootstrap b = new ServerBootstrap();
            // 分配
            b.group(bossGroup, workerGroup)
                    // 使用NIO
                    .channel(NioServerSocketChannel.class)
                    // 设置最大连接数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(
                                    // 解码器
                                    // 将Response对象编码为字节流
                                    // 处理的业务逻辑
                                    new MyServerHandler()
                            );
                        }
                    });

            // 启动以及关闭
            ChannelFuture f = null;
            f = b.bind(7397).sync();
            f.channel().closeFuture().sync();


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 优雅的关闭
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }
}
