package com.wjw.server.handler;

import com.wjw.proto.OtherConstants;
import com.wjw.proto.ProtoHead;
import com.wjw.server.config.CmdHandlerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wjw
 * @description: 请求分发
 * @title: StorageServerHandler
 * @date 2022/4/6 13:54
 */
public class StorageServerHandler extends ChannelInboundHandlerAdapter {

    protected static final Logger LOGGER = LoggerFactory.getLogger(StorageServerHandler.class);

    private ProtoHead header;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (header == null) {
            ByteBuf in = (ByteBuf) msg;
            if (in.readableBytes() < ProtoHead.HEAD_LENGTH) {
                return;
            }
            byte[] bytes = new byte[ProtoHead.HEAD_LENGTH];
            in.readBytes(bytes);
            header = ProtoHead.createFromBytes(bytes);
            // 根据协议命令生成不同处理器
            ChannelHandler handler = CmdHandlerFactory.createHandler(header.getCmd());
            ctx.pipeline().addLast(handler);

            // 将请求头设置进管道中，整个连接共享
            AttributeKey<ProtoHead> key = AttributeKey.valueOf(OtherConstants.ATTR_KEY_HEAD);
            ctx.channel().attr(key).set(header);
        }
        ctx.fireChannelRead(msg);
    }
}
