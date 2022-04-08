package com.wjw.handler;


import com.wjw.proto.OtherConstants;
import com.wjw.proto.ProtoHead;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wjw
 * @description: 附件处理器基类
 * @title: AttachBaseHandler
 * @date 2022/4/6 14:38
 */
public abstract class AttachBaseHandler extends SimpleChannelInboundHandler<ByteBuf> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AttachBaseHandler.class);

    /**
     * 是否已经初始化的标识符
     */
    private boolean flgInited;
    protected ChannelHandlerContext ctx;
    /**
     * 协议请求天
     */
    protected ProtoHead header;

    /**
     * 基类初始化方法
     *
     * @param ctx
     * @param msg
     * @return
     * @author wjw
     * @date 2022/4/6 14:44
     */
    private void baseInit(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        flgInited = true;
        this.ctx = ctx;
        // 处理请求头
        AttributeKey<ProtoHead> key = AttributeKey.valueOf(OtherConstants.ATTR_KEY_HEAD);
        header = ctx.channel().attr(key).get();
        init(ctx, msg);
    }

    /**
     * 初始化方法
     *
     * @param ctx
     * @param msg
     * @return
     * @author wjw
     * @date 2022/4/6 14:44
     */
    protected void init(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
    }

    /**
     * 此方法子类不应该重写，具体逻辑请写在read方法
     * @param ctx
     * @param msg
     * @return
     * @author wjw
     * @date 2022/4/6 14:53
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        if (!flgInited) {
            baseInit(ctx, msg);
        }
        read(ctx, msg);
    }
    /**
     * 子类实现，无需关注协议的请求头
     * @param ctx
     * @param msg
     * @return
     * @author wjw
     * @date 2022/4/7 14:41
     */
    protected abstract void read(ChannelHandlerContext ctx, ByteBuf msg) throws Exception;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        LOGGER.error("", cause);
        ctx.close();
    }
}
