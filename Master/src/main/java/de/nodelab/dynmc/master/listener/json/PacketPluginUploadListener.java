package de.nodelab.dynmc.master.listener.json;

import de.nodelab.dynmc.master.Main;
import de.nodelab.dynmc.master.PendingData;
import de.nodelab.dynmc.network.events.PacketListen;
import de.nodelab.dynmc.network.events.PacketListener;
import de.nodelab.dynmc.network.json.packet.in.PacketInPluginUpload;
import io.netty.channel.ChannelHandlerContext;

public class PacketPluginUploadListener extends PacketListener {

    @PacketListen
    public void onPluginUpload(ChannelHandlerContext ctx, PacketInPluginUpload packet) {
        Main.getInstance().getPendingData().put(packet.getName(), new PendingData("plugin", ctx));
    }

}
