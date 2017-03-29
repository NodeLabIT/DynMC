package de.nodelab.dynmc.daemon;

import de.nodelab.dynmc.network.NetClient;
import de.nodelab.dynmc.network.packet.PacketException;
import de.nodelab.dynmc.network.packet.PacketServerStart;
import de.nodelab.dynmc.network.packet.PacketServerStop;
import de.nodelab.dynmc.network.packet.PacketStatus;
import io.netty.channel.ChannelFuture;

public class Main {

    private NetClient client;

    public Main(String[] args) {
        this.client = NetClient.getInstance();
        this.client.getPacketRegistry()
                .add(0x01, PacketServerStart.class)
                .add(0x02, PacketServerStop.class)
                .add(0x03, PacketStatus.class)
                .add(0x04, PacketException.class);

        this.client.setClose(() -> System.out.println("Closed"));

        System.out.println("Starting client...");
        try {
            ChannelFuture f = this.client.start();
            PacketException ex = new PacketException();
            ex.setSource("Daemon");
            ex.setStacktrace("Fail");
            this.client.sendPacket(ex);
            System.out.println("Sent packet");
            f.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Main(args);
    }

}
