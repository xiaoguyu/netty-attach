package com.wjw.server.handler;

import com.wjw.proto.ProtoHead;
import com.wjw.storage.StorageUploadFileRequest;
import com.wjw.storage.StorePathResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author wjw
 * @description: 请求分发（目前是文件下载，每次传输数据都重开连接）
 * @title: StorageServerHandler
 * @date 2022/3/31 17:20
 */
public class StorageServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(StorageServerHandler.class);

    private ChannelHandlerContext ctx;

    private ProtoHead header;
    private StorageUploadFileRequest request;

    private String basePath = "D:\\attach\\attach";
    private FileOutputStream fos;
    private int readedLen = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        super.channelActive(ctx);
        File dir = new File(basePath);
        dir.mkdirs();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        // 处理请求头
        if (header == null) {
            if (in.readableBytes() < ProtoHead.HEAD_LENGTH) {
                return;
            }
            header = ProtoHead.createFromBytes(in);
        }
        // 处理请求body
        if (request == null) {
            if (in.readableBytes() < StorageUploadFileRequest.PARAM_LENGTH) {
                return;
            }
            request = StorageUploadFileRequest.createFromBytes(in, CharsetUtil.UTF_8);
        }
        // 处理附件
        int readableBytes = in.readableBytes();
        if (readableBytes > 0) {
            if (fos == null) {
                String filePath = basePath + File.separator + "testttt." + request.getFileExtName();
                System.out.println(filePath);
                File file = new File(filePath);
                if (!file.exists()) {
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
            }
            int len = readedLen + readableBytes > request.getFileSize() ? (int) request.getFileSize() - readedLen : readableBytes;
            byte[] fileByte = new byte[len];
            in.readBytes(fileByte);
            fos.write(fileByte);
            readedLen += len;
        }
        // 读取完成，关闭连接
        if (readedLen >= request.getFileSize()) {
            sendResponse();
            destroy();
        }
    }

    private void sendResponse() {
        StorePathResponse response = new StorePathResponse("ddd");
        ctx.write(response);
    }

    private void destroy() throws IOException {
        ctx.close();
        if (null != fos) {
            fos.close();
        }
        header = null;
        request = null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        LOGGER.error("", cause);
        ctx.close();
        if (null != fos) {
            fos.close();
        }
    }
}
