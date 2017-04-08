package de.nodelab.dynmc.network.json;

import de.nodelab.dynmc.network.*;
import de.nodelab.dynmc.network.json.packet.JsonPacket;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Setter;

public class NetJsonServer extends NetComponent<JsonPacket> {

    private static NetJsonServer instance;

    public static NetJsonServer getInstance() {
        if (instance == null) {
            instance = new NetJsonServer(1338);
        }
        return instance;
    }

    private EventLoopGroup bossGroup, workerGroup;
    private Channel channel;

    @Setter
    private Runnable close;

    private NetJsonServer(int port) {
        super(port);
    }

    @Override
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
                            System.out.println("Client connected");

                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new NetJsonEncoder());
                            pipeline.addLast(new NetJsonDecoder(NetJsonServer.this));
                            pipeline.addLast(new NetJsonPacketHandler(NetJsonServer.this));
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            //.childOption(ChannelOption.SO_BACKLOG, 50);

            this.channel = bootstrap.bind(this.getPort()).channel();
            ChannelFuture future = this.channel.closeFuture().addListener((ChannelFutureListener) (channelFuture) -> {
                NetJsonServer.this.bossGroup.shutdownGracefully();
                NetJsonServer.this.workerGroup.shutdownGracefully();
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
