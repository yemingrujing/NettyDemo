package chapter.four;

import com.google.protobuf.MessageLite;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

/**
 * 使用 protobuf
 *
 * @author Wei.Guang
 * @create 2019-03-20 17:28
 **/
public class ProtoBufInitializer extends ChannelInitializer<Channel> {

    private final MessageLite lite;

    public ProtoBufInitializer(MessageLite lite) {
        this.lite = lite;
    }

    @Override
    protected void initChannel(Channel ch) {
        ch.pipeline()
                // 添加 ProtobufVarint32FrameDecoder以分隔帧
                .addLast(new ProtobufVarint32FrameDecoder())
                // 添加 ProtobufEncoder以处理消息的编码
                .addLast(new ProtobufEncoder())
                // 添加 ProtobufDecoder以解码消息
                .addLast(new ProtobufDecoder(lite))
                // 添加 ObjectHandler 以处理解码消息
                .addLast(new ObjectHandler());
    }

    public static final class ObjectHandler extends SimpleChannelInboundHandler<Object> {

        @Override
        public void channelRead0(ChannelHandlerContext ctx, Object msg) {
            // Do something with the object
        }
    }
}
