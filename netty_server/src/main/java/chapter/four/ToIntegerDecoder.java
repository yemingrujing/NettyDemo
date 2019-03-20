package chapter.four;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 扩展 ByteToMessageDecoder 类，以将字节解码为特定的格式
 *
 * @author Wei.Guang
 * @create 2019-03-20 9:35
 **/
public class ToIntegerDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        // 检查是否至少有 4字节可读（一个 int的字节长度）
        if (byteBuf.readableBytes() >= 4) {
            // 从入站 ByteBuf 中读取一个 int，并将其添加到解码消息的 List 中
            list.add(byteBuf.readInt());
        }
    }
}
