package chapter.four;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * 使用 HTTPS
 *
 * @author Wei.Guang
 * @create 2019-03-20 13:51
 **/
public class HttpCodecInitializer extends ChannelInitializer<Channel> {

    private final SslContext context;
    private final boolean isClient;

    public HttpCodecInitializer(SslContext context, boolean isClient) {
        this.context = context;
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        SSLEngine engine = context.newEngine(ch.alloc());
        // 将 SslHandler 添加到ChannelPipeline 中以使用 HTTPS
        pipeline.addLast("ssl", new SslHandler(engine));

        if (isClient) {
            // 如果是客户端，则添加 HttpClientCodec
            pipeline.addLast("codec", new HttpClientCodec());
        } else {
            // 如果是服务器，则添
            // 加 HttpServerCodec
            pipeline.addLast("codec", new HttpServerCodec());
        }
    }
}
