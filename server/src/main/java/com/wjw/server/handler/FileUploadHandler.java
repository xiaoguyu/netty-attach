package com.wjw.server.handler;

import cn.hutool.core.util.RandomUtil;
import com.wjw.handler.AttachBaseHandler;
import com.wjw.server.config.NettyAttachConfig;
import com.wjw.storage.FileUploadRequest;
import com.wjw.storage.FileUploadResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author wjw
 * @description: 附件上传处理器
 * @title: FileUploadHandler
 * @date 2022/3/31 17:20
 */
public class FileUploadHandler extends AttachBaseHandler {

    private FileUploadRequest request;

    private String basePath = NettyAttachConfig.getBasePath();
    private FileOutputStream fos;
    private String fileName;
    private int readedLen = 0;

    @Override
    protected void init(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        File dir = new File(basePath);
        dir.mkdirs();
    }

    @Override
    protected void read(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        // 处理请求参数
        if (request == null) {
            if (in.readableBytes() < FileUploadRequest.PARAM_LENGTH) {
                return;
            }
            request = new FileUploadRequest();
            request.setHead(header);
            request.loadParamFromBytes(in, CharsetUtil.UTF_8);
        }
        // 处理附件
        int readableBytes = in.readableBytes();
        if (readableBytes > 0) {
            if (fos == null) {
                // 随机生成文件名
                fileName = RandomUtil.randomString(6) + "." + request.getFileExtName();
                String filePath = basePath + fileName;
                File file = new File(filePath);
                if (!file.exists()) {
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
            }
            int len = readedLen + readableBytes > request.getFileSize() ? (int) request.getFileSize() - readedLen : readableBytes;
            byte[] fileByte = new byte[len];
            in.readBytes(fileByte);
            fos.write(fileByte);
            readedLen += len;
        }
        // 读取完成，关闭连接
        if (readedLen >= request.getFileSize()) {
            sendResponse();
            destroy();
        }
    }
    /**
     * 响应结果
     * @return
     * @author wjw
     * @date 2022/4/7 14:46
     */
    private void sendResponse() {
        FileUploadResponse response = new FileUploadResponse(fileName);
        response.setSuccessHead();
        ctx.writeAndFlush(response);
    }
    /**
     * 销毁方法
     * @return
     * @author wjw
     * @date 2022/4/7 14:46
     */
    private void destroy() throws IOException {
        if (null != fos) {
            fos.close();
        }
        ctx.close();
        header = null;
        request = null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        if (null != fos) {
            fos.close();
        }
    }
}
