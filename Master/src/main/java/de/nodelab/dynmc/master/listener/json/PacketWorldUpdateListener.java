package de.nodelab.dynmc.master.listener.json;

import de.nodelab.dynmc.master.Main;
import de.nodelab.dynmc.master.db.obj.DatabaseWorld;
import de.nodelab.dynmc.network.events.PacketListen;
import de.nodelab.dynmc.network.events.PacketListener;
import de.nodelab.dynmc.network.json.packet.in.PacketInWorldUpdate;
import de.nodelab.dynmc.network.json.packet.out.PacketOutWorldUpdate;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class PacketWorldUpdateListener extends PacketListener {

    @PacketListen
    public void onWorldUpdate(ChannelHandlerContext ctx, PacketInWorldUpdate packet) {
        DatabaseWorld world = new DatabaseWorld();
        List<DatabaseWorld.AccessRule> accessRules = new ArrayList<>();
        if(!packet.getNewName().isEmpty()) {
            world.setName(packet.getNewName());
            accessRules.add(DatabaseWorld.AccessRule.NAME);
        }
        if(!packet.getDescription().isEmpty()) {
            world.setDescription(packet.getDescription());
            accessRules.add(DatabaseWorld.AccessRule.DESCRIPTION);
        }

        Main.getInstance().getDatabase().async().updateWorld(packet.getName(), world, () -> {
            ctx.writeAndFlush(new PacketOutWorldUpdate());
        }, accessRules.toArray(new DatabaseWorld.AccessRule[0]));
    }

}
