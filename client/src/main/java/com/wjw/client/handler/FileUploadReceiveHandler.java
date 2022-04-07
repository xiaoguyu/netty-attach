package com.wjw.client.handler;

import com.wjw.handler.AttachBaseHandler;
import com.wjw.storage.FileUploadResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

/**
 * @author wjw
 * @description: 文件上传响应处理器
 * @title: FileUploadReceiveHandler
 * @date 2022/4/1 11:16
 */
public class FileUploadReceiveHandler extends AttachBaseHandler {

    private FileUploadResponse response;

    @Override
    protected void read(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        // 处理请求参数
        if (response == null) {
            if (msg.readableBytes() < header.getContentLength()) {
                return;
            }
            response = new FileUploadResponse();
            response.setHead(header);
            response.loadParamFromBytes(msg, CharsetUtil.UTF_8);
            System.out.println("文件路径");
            System.out.println(response.getPath());
        }
    }
}
