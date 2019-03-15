package chapter.three;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 使用 Netty 的异步网络处理
 *
 * @author Wei.Guang
 * @create 2019-03-15 13:20
 **/
public class NettyNioServer {

    public void server(int port) throws Exception {
        final ByteBuf buf = Unpooled.copiedBuffer("Hi!\r\n", CharsetUtil.UTF_8);
        // 为非阻塞模式使用NioEventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(group).channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    //指定 ChannelInitializer， 对于每个已接受的连接都调用它
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 添加 ChannelInboundHandlerAdapter 以接收和处理事件
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) {
                                    System.out.println("server channelReadComplete....");
                                    // 将消息写到客户端，并添加ChannelFutureListener， 以便消息一被写完就关闭连接
                                    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
                                }

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    System.out.println("server channelRead.....;received: " + msg);
                                    ctx.write(msg);
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    System.out.println("server occur exception:" + cause.getMessage());
                                    cause.printStackTrace();
                                    ctx.close();
                                }
                            }).addLast(new StringDecoder(CharsetUtil.UTF_8)).addLast(new StringEncoder(Charset.forName("GBK")));
                        }
                    });
            // 绑定服务器以接受连接
            ChannelFuture f = b.bind().sync();
            System.out.println(NettyNioServer.class + " started and listen on " + f.channel().localAddress());
            f.channel().closeFuture().sync();
        } finally {
            // 释放所有资源
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyNioServer().server(65535);
    }
}
