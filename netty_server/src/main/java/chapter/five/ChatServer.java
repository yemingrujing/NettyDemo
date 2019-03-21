package chapter.five;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

import java.net.InetSocketAddress;

/**
 * 引导服务器
 *
 * @author Wei.Guang
 * @create 2019-03-21 13:19
 **/
public class ChatServer {

    /**
     * 创建DefaultChannelGroup，其将保存所有已经连接的WebSocket Channel
     */
    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    private final EventLoopGroup group = new NioEventLoopGroup();
    private Channel channel;

    public ChannelFuture start(InetSocketAddress address) {
        // 引导服务器
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group).channel(NioServerSocketChannel.class).childHandler(createInitializer(channelGroup));
        ChannelFuture future = bootstrap.bind(address);
        future.syncUninterruptibly();
        channel = future.channel();
        return future;
    }

    /**
     * 创建ChatServerInitializer
     * @param group
     * @return
     */
    protected ChannelInitializer<Channel> createInitializer(ChannelGroup group) {
        return new ChatServerInitializer(group);
    }

    /**
     * 处理服务器关闭，并释放所有的资源
     */
    public void destory() {
        if (channel != null) {
            channel.close();
        }
        channelGroup.close();
        group.shutdownGracefully();
    }

    public static void main(String[] args){
//        if (args.length != 1) {
//            System.err.println("Please give port as argument");
//            System.exit(1);
//        }
        int port = Integer.parseInt("8888");
        System.out.println("已连接。。。。。。。");
        final ChatServer endpoint = new ChatServer();
        ChannelFuture future = endpoint.start(new InetSocketAddress(port));
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                endpoint.destory();
            }
        });
        future.channel().closeFuture().syncUninterruptibly();
    }
}
