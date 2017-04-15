package de.nodelab.dynmc.master.listener.json;

import de.nodelab.dynmc.master.Main;
import de.nodelab.dynmc.network.events.PacketListen;
import de.nodelab.dynmc.network.events.PacketListener;
import de.nodelab.dynmc.network.json.packet.in.PacketInWorldRemove;
import de.nodelab.dynmc.network.json.packet.out.PacketOutWorldRemove;
import io.netty.channel.ChannelHandlerContext;

public class PacketWorldRemoveListener extends PacketListener {

    @PacketListen
    public void onWorldRemove(ChannelHandlerContext ctx, PacketInWorldRemove packet) {
        Main.getInstance().getDatabase().async().removeWorld(packet.getName(), () -> ctx.writeAndFlush(new PacketOutWorldRemove()));
    }

}
