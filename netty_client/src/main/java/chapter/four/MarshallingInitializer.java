package chapter.four;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

import java.io.Serializable;

/**
 * 使用 JBoss Marshalling
 *
 * @author Wei.Guang
 * @create 2019-03-20 17:18
 **/
public class MarshallingInitializer extends ChannelInitializer<Channel> {

    private final MarshallerProvider marshallerProvider;
    private final UnmarshallerProvider unmarshallerProvider;

    public MarshallingInitializer(MarshallerProvider marshallerProvider, UnmarshallerProvider unmarshallerProvider) {
        this.marshallerProvider = marshallerProvider;
        this.unmarshallerProvider = unmarshallerProvider;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                // 添加 MarshallingDecoder 以将 ByteBuf 转换为 POJO
                .addLast(new MarshallingDecoder(unmarshallerProvider))
                // 添加 MarshallingEncoder 以将POJO转换为 ByteBuf
                .addLast(new MarshallingEncoder(marshallerProvider))
                // 添加 ObjectHandler，以处理普通的实现了
                .addLast(new ObjectHandler());
    }

    public static final class ObjectHandler extends SimpleChannelInboundHandler<Serializable> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Serializable msg) {
            // Do something
        }
    }
}
