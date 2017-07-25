package de.nodelab.dynmc.master.listener;

import de.nodelab.dynmc.master.Main;
import de.nodelab.dynmc.master.db.obj.DatabaseDaemon;
import de.nodelab.dynmc.network.events.PacketListen;
import de.nodelab.dynmc.network.events.PacketListener;
import de.nodelab.dynmc.network.packet.client.PacketStatusResult;
import de.nodelab.dynmc.network.packet.server.PacketStatus;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

public class PacketStatusListener extends PacketListener {

    @PacketListen
    public void onPacketStatus(ChannelHandlerContext ctx, PacketStatus packet) {
        String ip = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
        System.out.println(ip);

        Main.getInstance().getDatabase().async().getDaemonByHost(ip, daemon -> {
           daemon.setCpu(packet.getCpuUsage());
           daemon.setRam(packet.getRamUsage());
           Main.getInstance().getDatabase().async().updateDaemon(daemon, () -> {
               ctx.writeAndFlush(new PacketStatusResult());
           }, DatabaseDaemon.AccessRule.RAM, DatabaseDaemon.AccessRule.CPU);
        });
    }

}
