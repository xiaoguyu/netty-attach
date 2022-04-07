package com.wjw.server.config;

import com.wjw.proto.CmdConstants;
import com.wjw.server.handler.FileDownloadHandler;
import com.wjw.server.handler.FileUploadHandler;
import io.netty.channel.ChannelHandler;

/**
 * @author wjw
 * @description: 处理器工厂，根据命令生成不同处理器
 * @title: StorageServerConfig
 * @date 2022/4/6 14:03
 */
public class CmdHandlerFactory {

    public static ChannelHandler createHandler(byte cmd) {
        switch (cmd) {
            case CmdConstants.STORAGE_PROTO_CMD_UPLOAD_FILE:
                return new FileUploadHandler();
            case CmdConstants.STORAGE_PROTO_CMD_DOWNLOAD_FILE:
                return new FileDownloadHandler();
            default:
                return null;
        }
    }

}
