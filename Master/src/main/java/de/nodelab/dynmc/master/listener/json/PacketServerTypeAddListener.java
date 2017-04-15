package de.nodelab.dynmc.master.listener.json;

import de.nodelab.dynmc.master.Main;
import de.nodelab.dynmc.master.db.obj.DatabaseServerType;
import de.nodelab.dynmc.network.events.PacketListen;
import de.nodelab.dynmc.network.events.PacketListener;
import de.nodelab.dynmc.network.json.packet.in.PacketInServerTypeAdd;
import de.nodelab.dynmc.network.json.packet.out.PacketOutServerTypeAdd;
import io.netty.channel.ChannelHandlerContext;

public class PacketServerTypeAddListener extends PacketListener {

    @PacketListen
    public void onServerTypeAdd(ChannelHandlerContext ctx, PacketInServerTypeAdd packet) {
        DatabaseServerType serverType = new DatabaseServerType();
        serverType.setName(packet.getName());
        serverType.setPlugins(packet.getPlugins());
        serverType.setWorlds(packet.getWorlds());
        serverType.setManual(packet.isManual());

        Main.getInstance().getDatabase().async().addServerType(serverType, () -> ctx.writeAndFlush(new PacketOutServerTypeAdd()));
    }

}
