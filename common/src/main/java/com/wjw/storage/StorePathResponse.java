package com.wjw.storage;

import com.wjw.proto.FdfsResponse;
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

    public StorePathResponse(String path) {
        this.path = path;
    }

    @Override
    public long getBodyLength(Charset charset) {
        return path.getBytes(charset).length;
    }

    @Override
    public void writeParam(ChannelHandlerContext ctx, Charset charset) {
        ctx.write(path.getBytes(charset));
    }

    @Override
    public void writeBody(ChannelHandlerContext ctx, Charset charset) {

    }
}
