package com.wjw.storage;

import com.wjw.proto.CmdConstants;
import com.wjw.proto.FdfsRequest;
import com.wjw.proto.OtherConstants;
import com.wjw.proto.ProtoHead;
import com.wjw.proto.mapper.BytesUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * @author wjw
 * @description: 文件上传命令
 * @title: StorageUploadFileRequest
 * @date 2022/3/31 15:16
 */
public class StorageUploadFileRequest extends FdfsRequest {

    private static final byte uploadCmd = CmdConstants.STORAGE_PROTO_CMD_UPLOAD_FILE;
    private static final byte uploadAppenderCmd = CmdConstants.STORAGE_PROTO_CMD_UPLOAD_APPENDER_FILE;

    /**
     * 参数长度
     */
    public static final int PARAM_LENGTH = 12;

    /**
     * 发送文件长度
     */
    private long fileSize;
    /**
     * 文件扩展名（长度6字节）
     */
    private String fileExtName;


    public StorageUploadFileRequest(long fileSize, String fileExtName) {
        this.fileSize = fileSize;
        this.fileExtName = fileExtName;
    }

    /**
     * 构造函数
     *
     * @param inputStream
     * @param fileExtName
     * @param fileSize
     * @param isAppenderFile
     */
    public StorageUploadFileRequest(FileInputStream inputStream, String fileExtName, long fileSize,
                                    boolean isAppenderFile) {
        super();
        this.inputFile = inputStream;
        this.fileSize = fileSize;
        this.fileExtName = fileExtName;
        if (isAppenderFile) {
            head = new ProtoHead(uploadAppenderCmd);
        } else {
            head = new ProtoHead(uploadCmd);
        }
    }

    public static StorageUploadFileRequest createFromBytes(ByteBuf in, Charset charset) throws UnsupportedEncodingException {
        long fileSize = in.readLong();
        byte[] fileExtName = new byte[OtherConstants.FDFS_FILE_EXT_NAME_MAX_LEN];
        in.readBytes(fileExtName);
        int byteEndIdx = fileExtName.length;
        for (int i = 0; i < fileExtName.length; i++) {
            if (fileExtName[i] == 0) {
                byteEndIdx = i;
                break;
            }
        }
        return new StorageUploadFileRequest(fileSize, new String(fileExtName, 0, byteEndIdx, charset.name()));
    }

    @Override
    public long getBodyLength(Charset charset) {
        // fileSize + fileExtName + 文件流长度
        return 8 + OtherConstants.FDFS_FILE_EXT_NAME_MAX_LEN + getFileSize();
    }

    @Override
    public void writeParam(ChannelHandlerContext ctx, Charset charset) {
        // 长度
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeLong(fileSize);
        // 文件扩展名
        buffer.writeBytes(BytesUtil.objString2Byte(fileExtName, OtherConstants.FDFS_FILE_EXT_NAME_MAX_LEN, charset));
        ctx.write(buffer);
    }

    public static byte getUploadCmd() {
        return uploadCmd;
    }

    public static byte getUploadAppenderCmd() {
        return uploadAppenderCmd;
    }

    @Override
    public long getFileSize() {
        return fileSize;
    }

    public String getFileExtName() {
        return fileExtName;
    }

    public void setFileExtName(String fileExtName) {
        this.fileExtName = fileExtName;
    }
}
