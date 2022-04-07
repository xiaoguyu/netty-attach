package com.wjw.client.handler;

import com.wjw.handler.AttachBaseHandler;
import com.wjw.proto.ErrorCodeConstants;
import com.wjw.storage.FileDownloadResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

/**
 * @author wjw
 * @description: 文件下载响应处理器
 * @title: FileDownloadReceiveHandler
 * @date 2022/4/6 15:35
 */
public class FileDownloadReceiveHandler extends AttachBaseHandler {

    private FileDownloadResponse response;

    @Override
    protected void read(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        // 处理请求参数
        if (response == null) {
            if (msg.readableBytes() < 4) {
                return;
            }
            response = new FileDownloadResponse();
            response.setHead(header);
            response.loadParamFromBytes(msg, CharsetUtil.UTF_8);
        }

        // 处理请求参数
        long contentLength = 0;
        if (header.getStatus() != ErrorCodeConstants.SUCCESS && (contentLength = header.getContentLength()) > 0) {
            byte[] errmsgBytes = new byte[(int)contentLength];
            msg.readBytes(errmsgBytes);
            System.out.println(new String(errmsgBytes, CharsetUtil.UTF_8));
        } else {
            // 这是成功返回的文件字节，还没想好怎么处理
            System.out.println("触发文件字节");
        }
    }
}
