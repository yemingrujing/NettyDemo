package chapter.four;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * Integer 参数转换为它的 String 表示
 *
 * @author Wei.Guang
 * @create 2019-03-20 10:13
 **/
public class IntegerToStringDecoder extends MessageToMessageDecoder<Integer> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, Integer integer, List<Object> list) {
        list.add(String.valueOf(integer));
    }
}
