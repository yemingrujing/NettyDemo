package chapter.four;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * 在服务器端支持 WebSocket
 *
 * @author Wei.Guang
 * @create 2019-03-20 15:15
 **/
public class WebSocketServerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) {
        ch.pipeline().addLast(
                new HttpServerCodec(),
                // 为握手提供聚合的HttpRequest
                new HttpObjectAggregator(65535),
                // 如果被请求的端点是"/websocket",则处理该升级握手
                new WebSocketServerProtocolHandler("/websocket"),
                // TextFrameHandler 处理TextWebSocketFrame
                new TextFrameHandler(),
                // BinaryFrameHandler 处理BinaryWebSocketFrame
                new BinaryFrameHandler(),
                // ContinuationFrameHandler 处理ContinuationWebSocketFrame
                new ContinuationFrameHandler());
    }

    public static final class TextFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
            // Handle text frame
        }
    }

    public static final class BinaryFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) {
            // Handle binary frame
        }
    }

    public static final class ContinuationFrameHandler extends SimpleChannelInboundHandler<ContinuationWebSocketFrame> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ContinuationWebSocketFrame msg) {
            // Handle continuation frame
        }
    }
}
