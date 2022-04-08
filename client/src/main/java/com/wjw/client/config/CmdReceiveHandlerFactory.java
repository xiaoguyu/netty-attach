package com.wjw.client.config;

import com.wjw.client.handler.FileDownloadReceiveHandler;
import com.wjw.client.handler.FileUploadReceiveHandler;
import com.wjw.proto.CmdConstants;
import io.netty.channel.ChannelHandler;

/**
 * @author wjw
 * @description: 处理器工厂，根据命令生成不同接收处理器
 * @title: CmdReceiveHandlerFactory
 * @date 2022/4/8 14:26
 */
public class CmdReceiveHandlerFactory {

    public static ChannelHandler createHandler(byte cmd) {
        switch (cmd) {
            case CmdConstants.STORAGE_PROTO_CMD_UPLOAD_FILE:
                return new FileUploadReceiveHandler();
            case CmdConstants.STORAGE_PROTO_CMD_DOWNLOAD_FILE:
                return new FileDownloadReceiveHandler();
            default:
                return null;
        }
    }
}
