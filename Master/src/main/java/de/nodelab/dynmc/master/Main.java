package de.nodelab.dynmc.master;

import de.nodelab.dynmc.network.NetServer;
import de.nodelab.dynmc.network.packet.PacketException;
import de.nodelab.dynmc.network.packet.PacketServerStart;
import de.nodelab.dynmc.network.packet.PacketServerStop;
import de.nodelab.dynmc.network.packet.PacketStatus;

public class Main {

    private NetServer netServer;

    private Main() {
        this.netServer = NetServer.getInstance();
        this.netServer.getPacketRegistry()
                .add(0x01, PacketServerStart.class)
                .add(0x02, PacketServerStop.class)
                .add(0x03, PacketStatus.class)
                .add(0x04, PacketException.class);

        this.netServer.setClose(() -> System.out.println("Closed"));

        System.out.println("Starting server...");
        try {
            this.netServer.start().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Main();
    }

}