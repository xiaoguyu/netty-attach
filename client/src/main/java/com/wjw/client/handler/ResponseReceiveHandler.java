package com.wjw.client.handler;

import com.wjw.proto.ProtoHead;
import com.wjw.storage.StorePathResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author wjw
 * @description: 响应接收处理器（未完成）
 * @title: ResponseReceiveHandler
 * @date 2022/4/1 11:16
 */
public class ResponseReceiveHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private ProtoHead header;
    private StorePathResponse response;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        byte[] bytes = new byte[in.readableBytes()];
        in.getBytes(0, bytes);
        // 处理请求头
        if (header == null) {
            if (in.readableBytes() < ProtoHead.HEAD_LENGTH) {
                return;
            }
            header = ProtoHead.createFromBytes(in);
            System.out.println(header);
            System.out.println(in);
        }
        // 处理响应参数
        if (response == null) {
            if (in.readableBytes() < header.getContentLength()) {
                return;
            }
            response = new StorePathResponse();
            response.setHead(header);
            response.loadParamFromBytes(in, CharsetUtil.UTF_8);
            System.out.println("文件路径");
            System.out.println(response.getPath());
        }
    }

}
