package de.nodelab.dynmc.network;

import de.nodelab.dynmc.network.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;

public class NetPacketHandler extends SimpleChannelInboundHandler<Packet> {

    private final NetComponent<Packet> component;

    public NetPacketHandler(NetComponent component) {
        this.component = component;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.component.handleChannelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.component.handleChannelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet p) throws Exception {
        this.component.getListenerRegistry().callEvent(channelHandlerContext, p);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(cause.getClass() == IOException.class) {
            return;
        }
        cause.printStackTrace();
    }
}
