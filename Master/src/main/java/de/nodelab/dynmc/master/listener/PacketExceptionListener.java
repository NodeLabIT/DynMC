package de.nodelab.dynmc.master.listener;

import de.nodelab.dynmc.network.events.PacketListen;
import de.nodelab.dynmc.network.events.PacketListener;
import de.nodelab.dynmc.network.packet.PacketException;
import io.netty.channel.ChannelHandlerContext;

public class PacketExceptionListener extends PacketListener {

    @PacketListen
    public void onException(ChannelHandlerContext ctx, PacketException packet) {
        System.out.println("Packet received!");
    }

}
