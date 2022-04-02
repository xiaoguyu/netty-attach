package com.wjw.proto;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author wjw
 * @description: 请求基类
 * @title: FdfsRequest
 * @date 2022/3/31 15:05
 */
public abstract class FdfsRequest extends FdfsProto {

    /**
     * 发送文件
     */
    protected FileInputStream inputFile;

    /**
     * 写入文件流
     *
     * @param ctx
     * @param charset
     * @return
     * @author wjw
     * @date 2022/4/1 11:59
     */
    public void writeBody(ChannelHandlerContext ctx, Charset charset) {
        if (null != inputFile) {
            try {
                // 零拷贝
                ChannelFuture f = ctx.write(new DefaultFileRegion(inputFile.getChannel(), 0, getFileSize()));
                f.addListener((ChannelFutureListener) future -> {
                    inputFile.close();
                });
            } catch (Exception e) {
                LOGGER.error("写入文件流失败", e);
                try {
                    inputFile.close();
                } catch (IOException ioException) {
                    LOGGER.error("写入文件流失败，源流关闭失败", ioException);
                }
            }
        }
    }

    /**
     * 附件大小
     *
     * @return
     * @author wjw
     * @date 2022/4/1 18:01
     */
    public abstract long getFileSize();
}
