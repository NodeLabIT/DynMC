package de.nodelab.dynmc.daemon;

import de.nodelab.dynmc.network.NetClient;
import de.nodelab.dynmc.network.packet.client.*;
import de.nodelab.dynmc.network.packet.server.*;
import io.netty.channel.ChannelFuture;

public class Main {

    private NetClient client;

    public Main(String[] args) {
        this.client = NetClient.getInstance();
        this.client.getInPacketRegistry()
                .add(0x01, PacketServerStart.class)
                .add(0x02, PacketServerStop.class)
                .add(0x03, PacketAuthResult.class)
                .add(0x04, PacketExceptionResult.class)
                .add(0x05, PacketStatusResult.class);
        this.client.getOutPacketRegistry()
                .add(0x01, PacketServerStartResult.class)
                .add(0x02, PacketServerStopResult.class)
                .add(0x03, PacketAuth.class)
                .add(0x04, PacketException.class)
                .add(0x05, PacketStatus.class);

        this.client.setClose(() -> System.out.println("Closed"));

        System.out.println("Starting client...");
        try {
            this.client.setStart(ctx -> {

            });
            ChannelFuture f = this.client.start();
            f.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    

    public static void main(String[] args) {
        new Main(args);
    }

}
