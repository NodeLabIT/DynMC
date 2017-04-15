package de.nodelab.dynmc.network;

import de.nodelab.dynmc.network.packet.Packet;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

public class NetClient extends NetComponent<Packet> {

    private static NetClient instance;

    public static NetClient getInstance() {
        if (instance == null) {
            instance = new NetClient(1337);
        }
        return instance;
    }

    @Getter
    @Setter
    private String host;

    private EventLoopGroup workerGroup;
    private Channel channel;

    @Setter
    private Runnable close;

    @Setter
    private Consumer<ChannelHandlerContext> start;

    private NetClient(int port) {
        super(port);
        this.host = "localhost";
    }

    @Override
    public ChannelFuture start() {
        this.workerGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(workerGroup)
                    .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            System.out.println("Client initialized");

                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new LengthFieldPrepender(4));
                            pipeline.addLast(new NetPacketEncoder(NetClient.this));
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Short.MAX_VALUE, 0, 4, 0, 4));
                            pipeline.addLast(new NetPacketDecoder(NetClient.this));
                            pipeline.addLast(new NetPacketHandler(NetClient.this));
                        }
                    });

            this.channel = bootstrap.connect(this.host, this.getPort()).sync().channel();
            ChannelFuture f = channel.closeFuture().addListener(future -> NetClient.this.workerGroup.shutdownGracefully());
            if(this.close != null) {
                f.addListener(future -> close.run());
            }
            return f;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void handleChannelActive(ChannelHandlerContext ctx) {
        this.start.accept(ctx);
    }

}
