package de.nodelab.dynmc.network;

import de.nodelab.dynmc.network.events.ListenerRegistry;
import de.nodelab.dynmc.network.packet.PacketRegistry;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

public abstract class NetComponent<T> {

    @Getter
    private PacketRegistry<T> outPacketRegistry;

    @Getter
    private PacketRegistry<T> inPacketRegistry;

    @Getter
    private ListenerRegistry<T> listenerRegistry;

    @Getter @Setter
    private int port;

    public NetComponent(int port) {
        this.port = port;
        this.outPacketRegistry = new PacketRegistry<>();
        this.listenerRegistry = new ListenerRegistry<>();
        this.inPacketRegistry = new PacketRegistry<>();
    }

    public void handleChannelActive(ChannelHandlerContext ctx) {
    }

    public void handleChannelInactive(ChannelHandlerContext ctx) {
    }

    public abstract ChannelFuture start();

}
