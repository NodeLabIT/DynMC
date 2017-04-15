package de.nodelab.dynmc.master.listener.json;

import de.nodelab.dynmc.master.Main;
import de.nodelab.dynmc.master.Utils;
import de.nodelab.dynmc.master.db.obj.DatabaseDaemon;
import de.nodelab.dynmc.network.events.PacketListen;
import de.nodelab.dynmc.network.events.PacketListener;
import de.nodelab.dynmc.network.json.packet.in.PacketInDaemonAdd;
import de.nodelab.dynmc.network.json.packet.out.PacketOutDaemonAdd;
import io.netty.channel.ChannelHandlerContext;

public class PacketDaemonAddListener extends PacketListener {

    @PacketListen
    public void onDaemonAdd(ChannelHandlerContext ctx, PacketInDaemonAdd packet) {
        DatabaseDaemon daemon = new DatabaseDaemon();
        daemon.setName(packet.getName());
        daemon.setHost(packet.getHost());
        daemon.setKey(Utils.randomKey());
        daemon.setMinPort(packet.getMinPort());
        daemon.setMaxPort(packet.getMaxPort());
        Main.getInstance().getDatabase().async().addDaemon(daemon, () -> {
            ctx.writeAndFlush(new PacketOutDaemonAdd());
        });
    }

}
