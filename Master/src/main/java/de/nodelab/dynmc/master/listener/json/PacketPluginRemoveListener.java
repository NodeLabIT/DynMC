package de.nodelab.dynmc.master.listener.json;

import de.nodelab.dynmc.master.Main;
import de.nodelab.dynmc.network.events.PacketListen;
import de.nodelab.dynmc.network.events.PacketListener;
import de.nodelab.dynmc.network.json.packet.in.PacketInPluginRemove;
import de.nodelab.dynmc.network.json.packet.out.PacketOutPluginRemove;
import io.netty.channel.ChannelHandlerContext;

public class PacketPluginRemoveListener extends PacketListener {

    @PacketListen
    public void onPluginRemove(ChannelHandlerContext ctx, PacketInPluginRemove packet) {
        Main.getInstance().getDatabase().async().removePlugin(packet.getName(), () -> ctx.writeAndFlush(new PacketOutPluginRemove()));
    }

}
