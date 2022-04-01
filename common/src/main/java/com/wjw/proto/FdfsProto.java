package com.wjw.proto;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * @author wjw
 * @description: 协议基类
 * @title: FdfsProto
 * @date 2022/4/1 14:00
 */
public abstract class FdfsProto {

    protected static final Logger LOGGER = LoggerFactory.getLogger(FdfsProto.class);

    /**
     * 报文头
     */
    protected ProtoHead head;

    /**
     * 获取报文头(包内可见)
     *
     * @return
     */
    ProtoHead getHead() {
        return head;
    }

    /**
     * 获取参数域长度
     *
     * @param charset 编码
     * @return
     * @date 2022/3/31 15:15
     */
    public abstract long getBodyLength(Charset charset);

    /**
     * 写入报文头
     *
     * @param ctx     netty上下文对象
     * @param charset 编码
     * @return
     * @author wjw
     * @date 2022/4/1 18:04
     */
    public void writeHead(ChannelHandlerContext ctx, Charset charset) {
        // 设置报文长度
        head.setContentLength(getBodyLength(charset));
        ctx.write(Unpooled.copiedBuffer(head.toByte()));
    }

    /**
     * 写入打包参数
     * 文件流单独写，避免内存中存在过多byte[]
     *
     * @param charset 编码
     * @return
     * @author wjw
     * @date 2022/3/31 15:39
     */
    public abstract void writeParam(ChannelHandlerContext ctx, Charset charset);

    /**
     * 写入body参数
     *
     * @param ctx     netty上下文对象
     * @param charset 编码
     * @return
     * @apiNote
     * @author wjw
     * @date 2022/4/1 14:04
     */
    public abstract void writeBody(ChannelHandlerContext ctx, Charset charset);

    /**
     * 将请求写入netty
     *
     * @param ctx     netty上下文对象
     * @param charset 编码
     * @return
     * @author wjw
     * @date 2022/4/1 18:03
     */
    public void writeBytes(ChannelHandlerContext ctx, Charset charset) {
        writeHead(ctx, charset);
        writeParam(ctx, charset);
        writeBody(ctx, charset);
    }
}
