package de.nodelab.dynmc.master.listener.json;

import de.nodelab.dynmc.master.Main;
import de.nodelab.dynmc.network.events.PacketListen;
import de.nodelab.dynmc.network.events.PacketListener;
import de.nodelab.dynmc.network.json.packet.in.PacketInDaemonRemove;
import de.nodelab.dynmc.network.json.packet.out.PacketOutDaemonRemove;
import io.netty.channel.ChannelHandlerContext;

public class PacketDaemonRemoveListener extends PacketListener {

    @PacketListen
    public void onDaemonRemove(ChannelHandlerContext ctx, PacketInDaemonRemove packet) {
        Main.getInstance().getDatabase().async().removeDaemon(packet.getName(), () -> ctx.writeAndFlush(new PacketOutDaemonRemove()));
    }

}
