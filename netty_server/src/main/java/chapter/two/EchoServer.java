package chapter.two;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * 引导服务器
 *
 * @author Wei.Guang
 * @create 2019-03-14 14:56
 **/
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        // 创建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    // 指定所使用的 NIO传输 Channel
                    .channel(NioServerSocketChannel.class)
                    // 使用指定的端口设置套接字地址
                    .localAddress(new InetSocketAddress(port))
                    // 添加一个EchoServerHandler 到子Channel的 ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            // EchoServerHandler 被标注为@Shareable，所以我们可以总是使用同样的实例
                            socketChannel.pipeline().addLast(serverHandler);
                        }
                    });
            // 异步地绑定服务器；调用 sync()方法阻塞等待直到绑定完成
            ChannelFuture f = b.bind().sync();
            // 获取 Channel 的CloseFuture，并且阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
        } finally {
            // 关闭 EventLoopGroup，释放所有的资源
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: " + EchoServer.class.getSimpleName() + "<port>");
        }
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }
}
