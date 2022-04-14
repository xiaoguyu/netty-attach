package com.wjw.client.manager;

import com.wjw.client.config.CmdReceiveHandlerFactory;
import com.wjw.client.config.NettyAttachConfig;
import com.wjw.client.handler.StorageClientHandler;
import com.wjw.handler.RequestSendHandler;
import com.wjw.proto.AttachRequest;
import com.wjw.proto.OtherConstants;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.AttributeKey;

/**
 * @author wjw
 * @description: 调度器
 * @title: AttachManager
 * @date 2022/4/6 13:46
 */
public class AttachManager {

    private static AttachManager instance;
    private String host;
    private int port;

    private AttachManager(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static AttachManager getInstance() {
        if (instance == null) {
            synchronized (AttachManager.class) {
                if (instance == null) {
                    String host = NettyAttachConfig.getHost();
                    if (host.equals("") || host.length() < 5) { // 长度随便定的
                        throw new RuntimeException("host can not null！");
                    }
                    instance = new AttachManager(host, NettyAttachConfig.getPort());
                }
            }
        }
        return instance;
    }

    public AttachFuture sendRequest(AttachRequest request) {
        AttachFuture future = new AttachFuture(request);
        // 此处应该优化成使用线程池
        EventLoopGroup worderGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(worderGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ChunkedWriteHandler(), new RequestSendHandler(), new StorageClientHandler());
                    // 根据不同请求设置不同的接收处理器
                    ChannelHandler handler = CmdReceiveHandlerFactory.createHandler(request.getHead().getCmd());
                    if (handler != null) {
                        ch.pipeline().addLast(handler);
                    }
                }
            });

            ChannelFuture f = b.connect(host, port).sync();
            Channel channel = f.channel();
            // 将回调钩子设置进管道
            AttributeKey<AttachFuture> key = AttributeKey.valueOf(OtherConstants.ATTR_KEY_FUTURE);
            channel.attr(key).set(future);

            channel.writeAndFlush(request);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            worderGroup.shutdownGracefully();
        }
        return future;
    }
}
