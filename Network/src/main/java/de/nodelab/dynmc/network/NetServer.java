package de.nodelab.dynmc.network;

import de.nodelab.dynmc.network.events.ListenerRegistry;
import de.nodelab.dynmc.network.packet.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.Getter;
import lombok.Setter;

public class NetServer {

    private static NetServer instance;

    public static NetServer getInstance() {
        if (instance == null) {
            instance = new NetServer(1337);
        }
        return instance;
    }

    @Getter @Setter
    private int port;

    @Getter
    private PacketRegistry packetRegistry;
    @Getter
    private ListenerRegistry listenerRegistry;

    private EventLoopGroup bossGroup, workerGroup;
    private Channel channel;

    @Setter
    private Runnable close;

    public NetServer(int port) {
        this.port = port;
        this.packetRegistry = new PacketRegistry();
        this.listenerRegistry = new ListenerRegistry();
    }

    public ChannelFuture start() {
        this.bossGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        this.workerGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new LengthFieldPrepender(4));
                            pipeline.addLast(new NetPacketEncoder());
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Short.MAX_VALUE, 0, 4, 0, 4));
                            pipeline.addLast(new NetPacketDecoder());
                            pipeline.addLast(new NetPacketHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_BACKLOG, 50);

            try {
                this.channel = bootstrap.bind(port).sync().channel();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ChannelFuture future = this.channel.closeFuture().addListener((ChannelFutureListener) (channelFuture) -> {
                NetServer.this.bossGroup.shutdownGracefully();
                NetServer.this.workerGroup.shutdownGracefully();
            });
            if (this.close != null) {
                return future.addListener((ChannelFutureListener) channelFuture -> this.close.run());
            }
            return future;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void stop() {
        this.channel.close();
    }

}
