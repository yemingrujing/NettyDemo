package chapter.four;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * 使用 TooLongFrameException 来通知 ChannelPipeline 中的其他 ChannelHandler 发生了帧大小溢出的
 *
 * @author Wei.Guang
 * @create 2019-03-20 10:19
 **/
public class SafeByteToMessageDecoder extends ByteToMessageDecoder {

    private static final int MAX_FRAME_SIZE = 1024;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        int readable = byteBuf.readableBytes();
        // 检查缓冲区中是否有超过 MAX_FRAME_SIZE个字节
        if (readable > MAX_FRAME_SIZE) {
            // 跳过所有的可读字节，抛出TooLongFrameException 并通知ChannelHandler
            byteBuf.skipBytes(readable);
            throw new TooLongFrameException("Frame too big");
        }
        // do something
    }
}
