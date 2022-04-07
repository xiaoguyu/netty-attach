package com.wjw.server.handler;

import com.wjw.handler.AttachBaseHandler;
import com.wjw.proto.ErrorCodeConstants;
import com.wjw.server.config.NettyAttachConfig;
import com.wjw.storage.FileDownloadRequest;
import com.wjw.storage.FileDownloadResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author wjw
 * @description: 附件下载处理器
 * @title: FileDownloadHandler
 * @date 2022/4/6 14:35
 */
public class FileDownloadHandler extends AttachBaseHandler {

    private FileDownloadRequest request;

    private FileInputStream inputFile;

    @Override
    protected void read(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        // 处理请求参数
        if (request == null) {
            if (msg.readableBytes() < FileDownloadRequest.PARAM_LENGTH) {
                return;
            }
            request = new FileDownloadRequest();
            request.loadParamFromBytes(msg, CharsetUtil.UTF_8);
        }
        sendResponse();
        destroy();
    }

    /**
     * 响应结果
     *
     * @return
     * @author wjw
     * @date 2022/4/7 14:45
     */
    private void sendResponse() throws FileNotFoundException {
        File file = new File(NettyAttachConfig.getBasePath() + request.getPath());
        FileDownloadResponse response = null;
        if (!file.exists()) {
            LOGGER.warn("file not exist:{}", file.getAbsolutePath());
            response = new FileDownloadResponse(ErrorCodeConstants.ERR_NO_ENOENT, "file not exist!");
        } else {
            response = new FileDownloadResponse(file.length(), new FileInputStream(file));
        }
        ctx.write(response);
    }

    /**
     * 销毁方法
     *
     * @return
     * @author wjw
     * @date 2022/4/7 14:45
     */
    private void destroy() throws IOException {
        ctx.close();
        header = null;
        request = null;
    }
}
