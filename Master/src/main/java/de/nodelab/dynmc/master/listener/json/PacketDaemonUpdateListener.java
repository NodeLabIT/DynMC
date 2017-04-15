package de.nodelab.dynmc.master.listener.json;

import de.nodelab.dynmc.master.Main;
import de.nodelab.dynmc.master.db.obj.DatabaseDaemon;
import de.nodelab.dynmc.network.events.PacketListen;
import de.nodelab.dynmc.network.events.PacketListener;
import de.nodelab.dynmc.network.json.packet.in.PacketInDaemonUpdate;
import de.nodelab.dynmc.network.json.packet.out.PacketOutDaemonUpdate;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class PacketDaemonUpdateListener extends PacketListener {

    @PacketListen
    public void onDaemonUpdate(ChannelHandlerContext ctx, PacketInDaemonUpdate packet) {
        DatabaseDaemon daemon = new DatabaseDaemon();
        List<DatabaseDaemon.AccessRule> accessRules = new ArrayList<>();
        if(!packet.getNewName().isEmpty()) {
            daemon.setName(packet.getNewName());
            accessRules.add(DatabaseDaemon.AccessRule.NAME);
        }
        if(!packet.getHost().isEmpty()) {
            daemon.setHost(packet.getHost());
            accessRules.add(DatabaseDaemon.AccessRule.HOST);
        }
        if(packet.getMinPort() != 0) {
            daemon.setMinPort(packet.getMinPort());
            accessRules.add(DatabaseDaemon.AccessRule.MIN_PORT);
        }
        if(packet.getMaxPort() != 0) {
            daemon.setMaxPort(packet.getMaxPort());
            accessRules.add(DatabaseDaemon.AccessRule.MAX_PORT);
        }
        Main.getInstance().getDatabase().async().updateDaemon(packet.getName(), daemon, () -> {
            ctx.writeAndFlush(new PacketOutDaemonUpdate());
        }, accessRules.toArray(new DatabaseDaemon.AccessRule[0]));
    }

}
