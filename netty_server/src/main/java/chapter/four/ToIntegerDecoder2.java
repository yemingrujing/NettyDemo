package chapter.four;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 扩展 ReplayingDecoder<Void>以将字节解码为消息
 *
 * @author Wei.Guang
 * @create 2019-03-20 9:58
 **/
public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {

    /**
     * 传入的 ByteBuf 是ReplayingDecoderByteBuf
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 从入站 ByteBuf 中读取一个 int，并将其添加到解码消息的 List 中
        list.add(byteBuf.readInt());
    }
}
