package de.nodelab.dynmc.network.json;

import de.nodelab.dynmc.network.NetComponent;
import de.nodelab.dynmc.network.json.packet.JsonPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;

public class NetJsonPacketHandler extends SimpleChannelInboundHandler<JsonPacket> {

    private final NetComponent<JsonPacket> component;

    public NetJsonPacketHandler(NetComponent component) {
        this.component = component;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.component.handleChannelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, JsonPacket p) throws Exception {
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
