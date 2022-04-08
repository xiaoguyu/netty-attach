package com.wjw.client.handler;

import com.wjw.client.manager.AttachFuture;
import com.wjw.handler.AttachBaseHandler;
import com.wjw.proto.OtherConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * @author wjw
 * @description: 响应处理器基类
 * @title: ReceiveBaseHandler
 * @date 2022/4/8 14:05
 */
public abstract class ReceiveBaseHandler extends AttachBaseHandler {

    protected AttachFuture future;

    @Override
    protected void init(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        AttributeKey<AttachFuture> key = AttributeKey.valueOf(OtherConstants.ATTR_KEY_FUTURE);
        future = ctx.channel().attr(key).get();
    }
}
