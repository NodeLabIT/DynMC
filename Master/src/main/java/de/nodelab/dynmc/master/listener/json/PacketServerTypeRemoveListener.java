package de.nodelab.dynmc.master.listener.json;

import de.nodelab.dynmc.master.Main;
import de.nodelab.dynmc.network.events.PacketListen;
import de.nodelab.dynmc.network.events.PacketListener;
import de.nodelab.dynmc.network.json.packet.in.PacketInServerTypeRemove;
import de.nodelab.dynmc.network.json.packet.out.PacketOutPluginRemove;
import de.nodelab.dynmc.network.json.packet.out.PacketOutServerTypeRemove;
import io.netty.channel.ChannelHandlerContext;

public class PacketServerTypeRemoveListener extends PacketListener {

    @PacketListen
    public void onServerTypeRemove(ChannelHandlerContext ctx, PacketInServerTypeRemove packet) {
        Main.getInstance().getDatabase().async().removeServerType(packet.getName(), () -> ctx.writeAndFlush(new PacketOutServerTypeRemove()));
    }

}
