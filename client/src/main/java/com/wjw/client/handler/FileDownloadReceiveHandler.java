package com.wjw.client.handler;

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
public class FileDownloadReceiveHandler extends ReceiveBaseHandler {

    // 成功：body = fileSize + fileBytes
    // 失败：body = errorMsg
    private FileDownloadResponse response;

    private byte[] fileBytes;
    private int readIdx;

    @Override
    protected void read(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        // 处理请求参数，请求成功和失败的处理不同
        if (header.isSuccess()) {
            // 处理文件长度
            if (response == null) {
                if (msg.readableBytes() < 4) {
                    return;
                }
                response = new FileDownloadResponse();
                response.setHead(header);
                response.loadParamFromBytes(msg, CharsetUtil.UTF_8);
            }
            // 处理文件流
            if (readIdx == 0) {
                fileBytes = new byte[(int) response.getFileSize()];
            }
            int readableBytes = msg.readableBytes();
            int length = 1 + readIdx + readableBytes > fileBytes.length ? fileBytes.length - readIdx - 1 : readableBytes;
            msg.readBytes(fileBytes, readIdx, length);
            readIdx += readableBytes;
            if (readIdx >= fileBytes.length - 1) {
                response.setFileBytes(fileBytes);
                future.done(response);
            }
        } else {
            // 处理异常信息
            response = new FileDownloadResponse();
            response.setHead(header);
            if (header.getContentLength() <= 0) {
                // future.done
            } else {
                byte[] errmsgBytes = new byte[(int) header.getContentLength()];
                msg.readBytes(errmsgBytes);
                response.setErrMsg(new String(errmsgBytes, CharsetUtil.UTF_8));
            }
        }
    }
}
