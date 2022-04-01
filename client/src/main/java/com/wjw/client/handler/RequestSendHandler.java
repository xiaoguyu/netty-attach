package com.wjw.client.handler;

import com.wjw.proto.FdfsProto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;

/**
 * @author wjw
 * @description: 请求发送处理器
 * @title: StorageClientHandler
 * @date 2022/4/1 11:07
 */
public class RequestSendHandler extends MessageToByteEncoder<FdfsProto> {

    @Override
    protected void encode(ChannelHandlerContext ctx, FdfsProto msg, ByteBuf out) throws Exception {
        msg.writeBytes(ctx, Charset.defaultCharset());

        ctx.flush();
    }
}
