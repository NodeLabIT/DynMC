package de.nodelab.dynmc.network;

import de.nodelab.dynmc.network.packet.Packet;
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

import java.util.HashMap;
import java.util.function.Consumer;

public class NetServer extends NetComponent<Packet> {

    private static HashMap<Integer, NetServer> instances = new HashMap<>();

    public static NetServer getInstance(int port) {
        if (!instances.containsKey(port)) {
            instances.put(port, new NetServer(port));
        }
        return instances.get(port);
    }

    private EventLoopGroup bossGroup, workerGroup;
    private Channel channel;

    @Setter
    private Runnable close;

    @Setter
    private Consumer<ChannelHandlerContext> clientDisconnected;

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

            this.channel = bootstrap.bind(this.getPort()).channel();
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

    @Override
    public void handleChannelInactive(ChannelHandlerContext ctx) {
        this.clientDisconnected.accept(ctx);
    }
}
