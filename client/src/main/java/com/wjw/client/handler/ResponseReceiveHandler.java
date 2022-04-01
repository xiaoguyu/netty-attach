package com.wjw.client.handler;

import com.wjw.proto.ProtoHead;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Arrays;

/**
 * @author wjw
 * @description: 响应接收处理器（未完成）
 * @title: ResponseReceiveHandler
 * @date 2022/4/1 11:16
 */
public class ResponseReceiveHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private ProtoHead header;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        byte[] bytes = new byte[in.readableBytes()];
        in.getBytes(0, bytes);
        System.out.println(Arrays.toString(bytes));
        System.out.println(in.readerIndex());
        // 处理请求头
        if (header == null) {
            if (in.readableBytes() < ProtoHead.HEAD_LENGTH) {
                return;
            }
            header = ProtoHead.createFromBytes(in);
            System.out.println(header);
            System.out.println(in);
        }
        System.out.println(in.readerIndex());
    }

}
