package chapter.five;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * 处理文本帧
 *
 * @author Wei.Guang
 * @create 2019-03-21 10:39
 **/
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final ChannelGroup group;

    public TextWebSocketFrameHandler(ChannelGroup group) {
        this.group = group;
    }

    /**
     * 重写 userEventTriggered()方法以处理自定义事件
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            // 如果该事件表示握手成功，则从该Channelipeline中移除 HttpRequestHandler，因为将不会接收到任何HTTP 消息了
            ctx.pipeline().remove(HttpRequestHandler.class);
            // 通知所有已经连接的WebSocket 客户端新的客户端已经连接上了
            group.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined"));
            // 将新的 WebSocket Channel添加到 ChannelGroup 中，以便它可以接收到所有的消息
            group.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        // 增加消息的引用计数，并将它写到 ChannelGroup 中所有已经连接的客户端
        group.writeAndFlush(msg.retain());
    }
}
