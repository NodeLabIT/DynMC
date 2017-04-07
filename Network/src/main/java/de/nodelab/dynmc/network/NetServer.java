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

public class NetServer extends NetComponent {

    private static NetServer instance;

    public static NetServer getInstance() {
        if (instance == null) {
            instance = new NetServer(1337);
        }
        return instance;
    }



    private EventLoopGroup bossGroup, workerGroup;
    private Channel channel;

    @Setter
    private Runnable close;

    private NetServer(int port) {
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
                            pipeline.addLast(new LengthFieldPrepender(4));
                            pipeline.addLast(new NetPacketEncoder(NetServer.this));
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Short.MAX_VALUE, 0, 4, 0, 4));
                            pipeline.addLast(new NetPacketDecoder(NetServer.this));
                            pipeline.addLast(new NetPacketHandler(NetServer.this));
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
                    //.childOption(ChannelOption.SO_BACKLOG, 50);

            try {
                this.channel = bootstrap.bind(this.getPort()).sync().channel();
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
