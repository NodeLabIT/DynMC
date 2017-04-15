package de.nodelab.dynmc.master.listener.json;

import de.nodelab.dynmc.master.Main;
import de.nodelab.dynmc.master.db.obj.DatabaseServerType;
import de.nodelab.dynmc.network.events.PacketListen;
import de.nodelab.dynmc.network.events.PacketListener;
import de.nodelab.dynmc.network.json.packet.in.PacketInServerTypeUpdate;
import de.nodelab.dynmc.network.json.packet.out.PacketOutServerTypeUpdate;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class PacketServerTypeUpdateListener extends PacketListener {

    @PacketListen
    public void onServerTypeUpdate(ChannelHandlerContext ctx, PacketInServerTypeUpdate packet) {
        DatabaseServerType serverType = new DatabaseServerType();
        List<DatabaseServerType.AccessRule> accessRules = new ArrayList<>();
        if(!packet.getNewName().isEmpty()) {
            serverType.setName(packet.getNewName());
            accessRules.add(DatabaseServerType.AccessRule.NAME);
        }
        if(packet.getPlugins() != null) {
            serverType.setPlugins(packet.getPlugins());
            accessRules.add(DatabaseServerType.AccessRule.PLUGINS);
        }
        if(packet.getWorlds() != null) {
            serverType.setWorlds(packet.getWorlds());
            accessRules.add(DatabaseServerType.AccessRule.WORLDS);
        }

        serverType.setManual(packet.isManual());
        accessRules.add(DatabaseServerType.AccessRule.MANUAL);

        Main.getInstance().getDatabase().async().updateServerType(packet.getName(), serverType, () -> {
            ctx.writeAndFlush(new PacketOutServerTypeUpdate());
        }, accessRules.toArray(new DatabaseServerType.AccessRule[0]));
    }

}
