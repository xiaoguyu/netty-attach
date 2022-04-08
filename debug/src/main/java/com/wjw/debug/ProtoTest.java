package com.wjw.debug;

import com.wjw.client.handler.FileUploadReceiveHandler;
import com.wjw.handler.RequestSendHandler;
import com.wjw.storage.FileUploadResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

/**
 * @author wjw
 * @description: 测试协议
 * @title: ProtoTest
 * @date 2022/4/1 14:35
 */
public class ProtoTest {

    public static void main(String[] args) throws Exception {
        EmbeddedChannel outChannel = new EmbeddedChannel(new RequestSendHandler());
        EmbeddedChannel inChannel = new EmbeddedChannel(new FileUploadReceiveHandler());

        String path = "C:\\Users\\wjw\\Desktop\\hotelQrCode.jpg";
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);


        FileUploadResponse response = new FileUploadResponse("111111.jpg");
        response.setSuccessHead();

        outChannel.writeOutbound(response);

        Object o = null;
        while ((o = outChannel.readOutbound()) != null) {
            ByteBuf b = (ByteBuf) o;
            byte[] bytes = new byte[b.readableBytes()];
            b.getBytes(0, bytes);
            System.out.println(Arrays.toString(bytes));
            inChannel.writeInbound(o);
        }

    }
}
