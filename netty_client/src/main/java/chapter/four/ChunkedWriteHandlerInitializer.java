package chapter.four;

import io.netty.channel.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileInputStream;

/**
 * 使用 ChunkedStream 传输文件内容
 *
 * @author Wei.Guang
 * @create 2019-03-20 16:58
 **/
public class ChunkedWriteHandlerInitializer extends ChannelInitializer<Channel> {

    private final File file;
    private final SslContext sslCtx;

    public ChunkedWriteHandlerInitializer(File file, SslContext sslCtx) {
        this.file = file;
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(Channel ch) {
        ch.pipeline()
                // 将 SslHandler 添加到ChannelPipeline 中
                .addLast(new SslHandler(sslCtx.newEngine(ch.alloc())))
                // 添加 ChunkedWriteHandler以处理作为ChunkedInput传入的数据
                .addLast(new ChunkedWriteHandler())
                // 一旦连接建立，WriteStreamHandler就开始写文件数据
                .addLast(new WriteStreamHandler());
    }

    public final class WriteStreamHandler extends ChannelInboundHandlerAdapter {

        /**
         * 当连接建立时，channelActive()方法将使用ChunkedInput写文件数据
         * @param ctx
         * @throws Exception
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            ctx.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
        }
    }
}
