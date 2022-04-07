package com.wjw.server.handler;

import com.wjw.proto.ProtoHead;
import com.wjw.server.config.CmdHandlerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wjw
 * @description: 求分发（目前是文件下载，每次传输数据都重开连接）
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
            in.getBytes(0, bytes);
            header = ProtoHead.createFromBytes(bytes);
            // 根据协议命令生成不同处理器
            ChannelHandler handler = CmdHandlerFactory.createHandler(header.getCmd());
            ctx.pipeline().addLast(handler);
        }
        ctx.fireChannelRead(msg);
    }
}
