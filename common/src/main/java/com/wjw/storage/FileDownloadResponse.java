package com.wjw.storage;

import com.wjw.proto.AttachResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.FileInputStream;
import java.nio.charset.Charset;

/**
 * @author wjw
 * @description: 文件下载响应
 * @title: FileDownloadResponse
 * @date 2022/4/6 16:12
 */
public class FileDownloadResponse extends AttachResponse {

    /**
     * 发送文件长度
     */
    private long fileSize;

    private byte[] fileBytes;

    public FileDownloadResponse() {
    }

    public FileDownloadResponse(long fileSize, FileInputStream inputFile) {
        this.fileSize = fileSize;
        this.inputFile = inputFile;
        setSuccessHead();
    }

    public FileDownloadResponse(byte errCode, String errMsg) {
        setErrorHead(errCode);
        this.errMsg = errMsg;
    }

    @Override
    public long getBodyLength(Charset charset) {
        if (errMsg == null) {
            // 文件长度 + 文件内容
            return 4 + fileSize;
        }
        return errMsg.getBytes(charset).length;
    }

    @Override
    public void writeParam(ChannelHandlerContext ctx, Charset charset) {
        if (errMsg == null) {
            ByteBuf buffer = ctx.alloc().buffer();
            buffer.writeLong(fileSize);
            ctx.write(buffer);
        }
    }

    @Override
    public void loadParamFromBytes(ByteBuf in, Charset charset) throws Exception {
        if (isSuccess()) {
            this.fileSize = in.readLong();
        }
    }

    @Override
    public long getFileSize() {
        return fileSize;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }
}
