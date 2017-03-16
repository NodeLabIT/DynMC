package de.nodelab.dynmc.network;

import de.nodelab.dynmc.network.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NetPacketHandler extends SimpleChannelInboundHandler {

    private final NetServer server;

    public NetPacketHandler() {
        this.server = NetServer.getInstance();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        if(o instanceof Packet) {
            Packet packet = (Packet) o;
            this.server.getListenerRegistry().callEvent(channelHandlerContext, packet);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
