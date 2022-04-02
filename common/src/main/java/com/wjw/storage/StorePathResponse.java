package com.wjw.storage;

import com.wjw.proto.FdfsResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.Charset;

/**
 * @author wjw
 * @description: 上传文件的响应参数
 * @title: StorePathResponse
 * @date 2022/4/1 14:06
 */
public class StorePathResponse extends FdfsResponse {

    private String path;

    public StorePathResponse() {
    }

    public StorePathResponse(String path) {
        this.path = path;
    }

    @Override
    public long getBodyLength(Charset charset) {
        return path.getBytes(charset).length;
    }

    @Override
    public void writeParam(ChannelHandlerContext ctx, Charset charset) {
        ctx.write(Unpooled.copiedBuffer(path.getBytes(charset)));
    }

    @Override
    public void writeBody(ChannelHandlerContext ctx, Charset charset) {

    }

    @Override
    public void loadParamFromBytes(ByteBuf in, Charset charset) throws Exception {
        byte[] pathBytes = new byte[(int) getHead().getContentLength()];
        in.readBytes(pathBytes);
        this.path = new String(pathBytes, charset);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
