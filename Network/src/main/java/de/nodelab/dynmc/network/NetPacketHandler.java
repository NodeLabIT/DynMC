package de.nodelab.dynmc.network;

import de.nodelab.dynmc.network.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;

public class NetPacketHandler extends SimpleChannelInboundHandler {

    private final NetComponent component;

    public NetPacketHandler(NetComponent component) {
        this.component = component;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.component.handleChannelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        if(o instanceof Packet) {
            Packet packet = (Packet) o;
            this.component.getListenerRegistry().callEvent(channelHandlerContext, packet);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(cause.getClass() == IOException.class) {
            return;
        }
        cause.printStackTrace();
    }
}
