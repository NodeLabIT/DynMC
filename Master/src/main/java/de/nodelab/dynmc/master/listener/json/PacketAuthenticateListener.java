package de.nodelab.dynmc.master.listener.json;

import de.nodelab.dynmc.network.events.PacketListen;
import de.nodelab.dynmc.network.events.PacketListener;
import de.nodelab.dynmc.network.json.packet.PacketAuthenticate;
import de.nodelab.dynmc.network.json.packet.PacketOk;
import io.netty.channel.ChannelHandlerContext;

public class PacketAuthenticateListener extends PacketListener {

    @PacketListen
    public void onAuth(ChannelHandlerContext ctx, PacketAuthenticate packet) {
        if(packet.getKey().equalsIgnoreCase("authkey")) {           // TODO Don't hardcode ;)
            ctx.writeAndFlush(new PacketOk());                                   // TODO Implement packet security
        } else {
            ctx.disconnect();
        }
    }

}
