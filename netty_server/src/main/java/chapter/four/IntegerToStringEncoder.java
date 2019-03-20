package chapter.four;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author Wei.Guang
 * @create 2019-03-20 10:42
 **/
public class IntegerToStringEncoder extends MessageToMessageEncoder<Integer> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Integer integer, List<Object> list) {
        // 将 Integer 转换为 String，并将其添加到 List 中
        list.add(String.valueOf(integer));
    }
}
