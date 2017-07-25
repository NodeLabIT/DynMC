package de.nodelab.dynmc.master.listener;

import de.nodelab.dynmc.master.Main;
import de.nodelab.dynmc.master.db.obj.DatabaseDaemon;
import de.nodelab.dynmc.network.events.PacketListen;
import de.nodelab.dynmc.network.events.PacketListener;
import de.nodelab.dynmc.network.packet.client.PacketAuthResult;
import de.nodelab.dynmc.network.packet.server.PacketAuth;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

public class PacketAuthListener extends PacketListener {

    @PacketListen
    public void onPacketAuth(ChannelHandlerContext ctx, PacketAuth packet) {
        String ip = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();

        Main.getInstance().getDatabase().async().getDaemonByHost(ip, daemon -> {
            if(daemon.getKey().equals(packet.getKey())) {
                ctx.writeAndFlush(new PacketAuthResult());

                Main.getInstance().getDaemons().put(daemon.getName(), ctx);
            } else {
                ctx.disconnect();
            }
        }, DatabaseDaemon.AccessRule.ALL);
    }

}
