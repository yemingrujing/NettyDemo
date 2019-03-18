package chapter.three;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Wei.Guang
 * @create 2019-03-18 12:19
 **/
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    ByteBuf buf;

    public EchoServerHandler(ByteBuf buf) {
        this.buf = buf;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("server channelRead...; received:" + msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("server channelReadComplete..");
        // 第一种方法：写一个空的buf，并刷新写出区域。完成后关闭sock channel连接。
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("server occur exception:" + cause.getMessage());
        cause.printStackTrace();
        ctx.close(); // 关闭发生异常的连接
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("server channelReadComplete....");
        // 将消息写到客户端，并添加ChannelFutureListener， 以便消息一被写完就关闭连接
        // 不要用buf.duplicate()，客户端第二次调用报错：io.netty.util.IllegalReferenceCountException: refCnt: 0
        ctx.writeAndFlush(buf.copy());
    }
}
