package chapter.three;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 使用 Netty 的阻塞网络处理
 *
 * @author Wei.Guang
 * @create 2019-03-15 11:38
 **/
public class NettyOioServer {

    public void server(int port) throws InterruptedException {
        final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\n\r", Charset.forName("UTF-8")));
        EventLoopGroup group = new OioEventLoopGroup();
        try {
            // 创建 ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    // 使用 OioEventLoopGroup以允许阻塞模式（旧的I/O）
                    .channel(OioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    // 指定 ChannelInitializer， 对于每个已接受的连接都调用它
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(
                                // 添加一个 ChannelInboundHandlerAdapter 以拦截和 处理事件
                                new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                        // 将消息写到客户端，并添加 ChannelFutureListener，以便消息一被写完就关闭连接
                                        ctx.writeAndFlush(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);
                                    }
                                }
                            );
                        }
                    });
            // 绑定服务器以接收连接
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        } finally {
            // 释放所有资源
            group.shutdownGracefully().sync();
        }
    }
}
