package chapter.four;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * 使用 ChannelInitializer 安装解码器
 *
 * @author Wei.Guang
 * @create 2019-03-20 16:12
 **/
public class CmdHandlerInitializer extends ChannelInitializer<Channel> {

    final byte SPACE = (byte) ' ';

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                // 添加 CmdDecoder 以提取Cmd 对象，并将它转发给下一个ChannelInboundHandler
                .addLast(new CmdDecoder(64 * 1024))
                // 添加 CmdHandler 以接收和处理 Cmd 对象
                .addLast(new CmdHandler());

    }

    public static final class Cmd {
        private final ByteBuf name;
        private final ByteBuf agrs;

        public Cmd(ByteBuf name, ByteBuf agrs) {
            this.name = name;
            this.agrs = agrs;
        }

        public ByteBuf getName() {
            return name;
        }

        public ByteBuf getAgrs() {
            return agrs;
        }
    }

    public final class CmdDecoder extends LineBasedFrameDecoder {
        public CmdDecoder(int maxLength) {
            super(maxLength);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
            // 从 ByteBuf 中提取由行尾符序列分隔的帧
            ByteBuf frame = (ByteBuf) super.decode(ctx, buffer);
            if (frame == null) {
                // 如果输入中没有帧，则返回 null
                return null;
            }
            // 查找第一个空格字符的索引。前面是命令名称，接着是参数
            int index = frame.indexOf(frame.readerIndex(), frame.writerIndex(), SPACE);
            // 使用包含有命令名称和参数的切片创建新的Cmd 对象
            return new Cmd(frame.slice(frame.readerIndex(), index), frame.slice(index + 1, frame.writerIndex()));
        }
    }

    public static final class CmdHandler extends SimpleChannelInboundHandler<Cmd> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Cmd msg) throws Exception {
            // Do something with the command
            // 处理传经 ChannelPipeline的 Cmd 对象
        }
    }
}
