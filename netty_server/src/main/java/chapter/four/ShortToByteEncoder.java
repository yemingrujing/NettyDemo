package chapter.four;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Wei.Guang
 * @create 2019-03-20 10:40
 **/
public class ShortToByteEncoder extends MessageToByteEncoder<Short> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Short aShort, ByteBuf byteBuf) {
        // 将 Short 写入ByteBuf 中
        byteBuf.writeShort(aShort);
    }
}
