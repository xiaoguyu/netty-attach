package com.wjw.storage;

import com.wjw.proto.AttachRequest;
import com.wjw.proto.CmdConstants;
import com.wjw.proto.OtherConstants;
import com.wjw.proto.ProtoHead;
import com.wjw.proto.mapper.BytesUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.Charset;

/**
 * @author wjw
 * @description: 文件下载请求
 * @title: FileDownloadRequest
 * @date 2022/4/6 14:56
 */
public class FileDownloadRequest extends AttachRequest {

    /**
     * 参数长度
     */
    public static final int PARAM_LENGTH = 21;

    /**
     * 开始位置
     */
    private long fileOffset;
    /**
     * 读取文件长度(大于0则是断电续传，等于0则是全部下载)
     */
    private long downloadBytes;
    /**
     * 文件路径(固定长度： 文件名 + . + 扩展名， 6 + 1 + 6 = 13)
     */
    private String path;

    public FileDownloadRequest() {
    }

    public FileDownloadRequest(String path) {
        this(0, 0, path);
    }

    public FileDownloadRequest(long fileOffset, long downloadBytes, String path) {
        this.fileOffset = fileOffset;
        this.downloadBytes = downloadBytes;
        this.path = path;
        head = new ProtoHead(CmdConstants.STORAGE_PROTO_CMD_DOWNLOAD_FILE);
    }

    @Override
    public long getBodyLength(Charset charset) {
//        return 8 + 13;
        return 21;
    }

    @Override
    public void writeParam(ChannelHandlerContext ctx, Charset charset) {
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeLong(fileOffset);
        buffer.writeLong(downloadBytes);
        buffer.writeBytes(path.getBytes(charset));
        ctx.write(buffer);
    }

    @Override
    public void loadParamFromBytes(ByteBuf in, Charset charset) throws Exception {
        this.fileOffset = in.readLong();
        this.downloadBytes = in.readLong();
        byte[] pathBytes = new byte[OtherConstants.FDFS_FILE_PATH_LEN];
        in.readBytes(pathBytes);
        this.path = BytesUtil.byte2EffectiveString(pathBytes, charset);
    }

    @Override
    public long getFileSize() {
        return 0;
    }

    public long getFileOffset() {
        return fileOffset;
    }

    public long getDownloadBytes() {
        return downloadBytes;
    }

    public String getPath() {
        return path;
    }
}
