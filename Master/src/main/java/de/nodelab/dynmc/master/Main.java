package de.nodelab.dynmc.master;

import de.nodelab.dynmc.master.listener.PacketExceptionListener;
import de.nodelab.dynmc.master.listener.json.PacketAuthenticateListener;
import de.nodelab.dynmc.network.NetServer;
import de.nodelab.dynmc.network.json.NetJsonServer;
import de.nodelab.dynmc.network.json.packet.PacketAuthenticate;
import de.nodelab.dynmc.network.json.packet.PacketOk;
import de.nodelab.dynmc.network.packet.PacketException;
import de.nodelab.dynmc.network.packet.PacketServerStart;
import de.nodelab.dynmc.network.packet.PacketServerStop;
import de.nodelab.dynmc.network.packet.PacketStatus;

public class Main {

    private NetServer netServer;
    private NetJsonServer netJsonServer;

    private Main() {
        this.netServer = NetServer.getInstance();
        this.netServer.getPacketRegistry()
                .add(0x01, PacketServerStart.class)
                .add(0x02, PacketServerStop.class)
                .add(0x03, PacketStatus.class)
                .add(0x04, PacketException.class);

        this.netJsonServer = NetJsonServer.getInstance();
        this.netJsonServer.getPacketRegistry()
                .add(0x01, PacketAuthenticate.class)
                .add(0x02, PacketOk.class);

        this.netServer.setClose(() -> System.out.println("Closed"));
        this.netJsonServer.setClose(() ->  System.out.println("JSON Server closed"));

        this.netServer.getListenerRegistry().register(new PacketExceptionListener());
        this.netJsonServer.getListenerRegistry().register(new PacketAuthenticateListener());

        System.out.println("Starting servers...");
        try {
            this.netJsonServer.start();
            this.netServer.start().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Main();
    }

}
