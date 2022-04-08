package com.wjw.proto;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author wjw
 * @description: 请求响应
 * @title: AttachResponse
 * @date 2022/4/1 13:56
 */
public abstract class AttachResponse extends AttachProto {

    /**
     * 异常信息
     */
    protected String errMsg;
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
     * @date 2022/4/1 11:59
     */
    public void writeBody(ChannelHandlerContext ctx, Charset charset) {
        if (errMsg == null) {
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
        } else {
            ByteBuf buffer = Unpooled.copiedBuffer(errMsg.getBytes(charset));
            ctx.write(buffer);
        }
    }

    /**
     * 附件大小
     *
     * @return
     * @date 2022/4/1 18:01
     */
    public abstract long getFileSize();

    public boolean isSuccess() {
        return head.isSuccess();
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
