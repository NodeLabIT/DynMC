package de.nodelab.dynmc.master.listener.json;

import de.nodelab.dynmc.network.events.PacketListen;
import de.nodelab.dynmc.network.events.PacketListener;
import de.nodelab.dynmc.network.json.packet.in.PacketInAuth;
import de.nodelab.dynmc.network.json.packet.out.PacketOutAuth;
import io.netty.channel.ChannelHandlerContext;

public class PacketAuthenticateListener extends PacketListener {

    @PacketListen
    public void onAuth(ChannelHandlerContext ctx, PacketInAuth packet) {
        if(packet.getKey().equalsIgnoreCase("authkey")) {           // TODO Don't hardcode ;)
            ctx.writeAndFlush(new PacketOutAuth());                                // TODO Implement packet security
        } else {
            ctx.disconnect();
        }
    }

}
