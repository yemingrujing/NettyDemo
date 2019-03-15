package chapter.two;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * Echo客户端
 *
 * @author Wei.Guang
 * @create 2019-03-14 15:54
 **/
@Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 当被通知 Channel是活跃的时候，发送一条消息
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    /**
     * 记录已接收消息的转储
     * @param channelHandlerContext
     * @param byteBuf
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        System.out.println("Client received: " + byteBuf.toString(CharsetUtil.UTF_8));
    }

    /**
     * 在发生异常时，记录错误并关闭
     * Channel
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
