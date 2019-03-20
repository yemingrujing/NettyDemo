package chapter.four;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * 处理由行尾符分隔的帧
 *
 * @author Wei.Guang
 * @create 2019-03-20 16:04
 **/
public class LineBasedHandlerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) {
        ch.pipeline()
                // 该 LineBasedFrameDecoder将提取的帧转发给下一个 ChannelInboundHandler
                // .addLast(new LineBasedFrameDecoder(64 * 1024))
                // 使用 LengthFieldBasedFrameDecoder 解码将帧长度编码到帧起始的前 8 个字节中的消息
                .addLast(new LengthFieldBasedFrameDecoder(64 * 1024, 0, 8))
                // 添加 FrameHandler以接收帧
                .addLast(new FrameHandler());
    }

    public static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuf> {

        /**
         * 传入了单个帧的内容
         * @param ctx
         * @param msg
         */
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
            // Do something with the data extracted from the frame
        }
    }
}
