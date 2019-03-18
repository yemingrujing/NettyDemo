package chapter.three;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 请求连接服务器的客户端
 *
 * @author Wei.Guang
 * @create 2019-03-15 15:37
 **/
public class NettyCommentClient {

    private final String host;
    private final int port;

    private NettyCommentClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public NettyCommentClient(int port) {
        this("localhost", port);
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            // 注册线程池
            b.group(group)
                    // 使用NioSocketChannel来作为连接用的channel类
                    .channel(NioSocketChannel.class)
                    // 绑定连接端口和host信息
                    .remoteAddress(new InetSocketAddress(this.host, this.port))
                    // 绑定连接初始化器
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            System.out.println("connected.......2");
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            System.out.println("created.......1");

            // 异步连接服务器
            ChannelFuture f = b.connect().sync();
            // 连接完成
            System.out.println("connected........3");
            // 异步等待关闭连接channel
            f.channel().closeFuture().sync();
            // 关闭完成
            System.out.println("closed........9");
        } finally {
            // 释放线程池资源
            group.shutdownGracefully().sync();
        }
    }
    
    public static void main(String[] args) throws Exception {
        new NettyCommentClient("127.0.0.1", 65535).start();
    }
}
