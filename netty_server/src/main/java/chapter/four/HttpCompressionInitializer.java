package chapter.four;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 自动压缩 HTTP 消息
 *
 * @author Wei.Guang
 * @create 2019-03-20 13:39
 **/
public class HttpCompressionInitializer extends ChannelInitializer<Channel> {

    private final boolean isClient;

    public HttpCompressionInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        if (isClient) {
            // 如果是客户端，则添加 HttpClientCodec
            pipeline.addLast("codec", new HttpClientCodec());
            // 如果是客户端，则添加HttpContentDecompressor 以处理来自服务器的压缩内容
            pipeline.addLast("decompressor", new HttpContentCompressor());
        } else {
            // 如果是服务器，则添加 HttpServerCodec
            pipeline.addLast("codec", new HttpServerCodec());
            // 如果是服务器，则添加HttpContentCompressor来压缩数据（ 如果客户端支持它）
            pipeline.addLast("compressor", new HttpContentCompressor());
        }
    }
}
