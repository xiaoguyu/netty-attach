package com.wjw.proto;

import com.wjw.proto.mapper.BytesUtil;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;

/**
 * FDFS协议头定义
 * <p>
 * <pre>
 * FDFS协议头一共10位
 * </pre>
 */
public class ProtoHead {

    /**
     * 报文长度
     */
    public static final int HEAD_LENGTH = OtherConstants.FDFS_PROTO_PKG_LEN_SIZE + 2;


    /**
     * 报文内容长度1-7位
     */
    private long contentLength = 0;
    /**
     * 报文类型8位
     * 在响应中，为状态码
     */
    private byte cmd;
    /**
     * 处理状态9位
     * 下响应中，为错误码
     */
    private byte status = (byte) 0;


    /**
     * 请求报文构造函数
     */
    public ProtoHead(byte cmd) {
        super();
        this.cmd = cmd;
    }

    public ProtoHead(byte cmd, byte status) {
        super();
        this.cmd = cmd;
        this.status = status;
    }

    /**
     * 返回报文构造函数
     *
     * @param contentLength
     * @param cmd
     * @param status
     */
    public ProtoHead(long contentLength, byte cmd, byte status) {
        super();
        this.contentLength = contentLength;
        this.cmd = cmd;
        this.status = status;
    }


    /**
     * 读取输入字节创建报文头
     * 注意：成功之后readIndex会后移
     * @param in
     * @return
     */
    public static ProtoHead createFromBytes(ByteBuf in) {
        if (!canCreate(in)) {
            throw new RuntimeException("构建请求头失败");
        }

        long returnContentLength = in.readLong();
        byte returnCmd = in.readByte();
        byte returnStatus = in.readByte();
        // 返回解析出来的ProtoHead
        return new ProtoHead(returnContentLength, returnCmd, returnStatus);
    }

    /**
     * 能否创建请求头信息
     * @param in
     * @return
     * @author wjw
     * @date 2022/3/31 17:15
     */
    public static boolean canCreate(ByteBuf in) {
        if (in.readableBytes() >= HEAD_LENGTH) {
            return true;
        }
        return false;
    }


    /**
     * toByte
     *
     * @return
     */
    public byte[] toByte() {
        byte[] header;
        byte[] hex_len;

        header = new byte[HEAD_LENGTH];
        Arrays.fill(header, (byte) 0);
        hex_len = BytesUtil.long2buff(contentLength);
        System.arraycopy(hex_len, 0, header, 0, hex_len.length);
        header[OtherConstants.PROTO_HEADER_CMD_INDEX] = cmd;
        header[OtherConstants.PROTO_HEADER_STATUS_INDEX] = status;
        return header;
    }

    @Override
    public String toString() {
        return "ProtoHead [contentLength=" + contentLength + ", cmd=" + cmd + ", status=" + status + "]";
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public static int getHeadLength() {
        return HEAD_LENGTH;
    }

    public long getContentLength() {
        return contentLength;
    }

    public byte getCmd() {
        return cmd;
    }

    public byte getStatus() {
        return status;
    }
}
