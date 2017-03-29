package de.nodelab.dynmc.network;

import de.nodelab.dynmc.network.events.ListenerRegistry;
import de.nodelab.dynmc.network.packet.PacketRegistry;
import io.netty.channel.ChannelFuture;
import lombok.Getter;
import lombok.Setter;

public abstract class NetComponent {

    @Getter
    private PacketRegistry packetRegistry;
    @Getter
    private ListenerRegistry listenerRegistry;

    @Getter @Setter
    private int port;

    public NetComponent(int port) {
        this.port = port;
        this.packetRegistry = new PacketRegistry();
        this.listenerRegistry = new ListenerRegistry();
    }

    public abstract ChannelFuture start();

}
