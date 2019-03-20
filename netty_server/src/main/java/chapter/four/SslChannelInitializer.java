package chapter.four;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * 添加 SSL/TLS 支持
 *
 * @author Wei.Guang
 * @create 2019-03-20 11:55
 **/
public class SslChannelInitializer extends ChannelInitializer<Channel> {

    /**
     * 传入要使用的SslContext
     */
    private final SslContext context;

    /**
     * 如果设置为 true，第一个写入的消息将不会被加密（客户端应该设置为 true）
     */
    private final boolean startTls;

    public SslChannelInitializer(SslContext context, boolean startTls) {
        this.context = context;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(Channel ch) {
        // 对于每个 SslHandler 实例，都使用 Channel 的 ByteBufAllocator 从 SslContext 获取一个新的 SSLEngine
        SSLEngine engine = context.newEngine(ch.alloc());
        // 将 SslHandler 作为第一个ChannelHandler 添加到ChannelPipeline 中
        ch.pipeline().addFirst("ssl", new SslHandler(engine, startTls));
    }
}
