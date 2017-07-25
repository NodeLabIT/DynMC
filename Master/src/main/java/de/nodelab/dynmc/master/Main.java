package de.nodelab.dynmc.master;

import de.nodelab.dynmc.master.db.DatabaseConnection;
import de.nodelab.dynmc.master.listener.PacketAuthListener;
import de.nodelab.dynmc.master.listener.PacketExceptionListener;
import de.nodelab.dynmc.master.listener.PacketRawDataListener;
import de.nodelab.dynmc.master.listener.PacketStatusListener;
import de.nodelab.dynmc.master.listener.json.*;
import de.nodelab.dynmc.network.NetServer;
import de.nodelab.dynmc.network.events.ListenerRegistry;
import de.nodelab.dynmc.network.json.NetJsonServer;
import de.nodelab.dynmc.network.json.packet.JsonPacket;
import de.nodelab.dynmc.network.json.packet.in.*;
import de.nodelab.dynmc.network.json.packet.out.*;
import de.nodelab.dynmc.network.packet.*;
import de.nodelab.dynmc.network.packet.client.*;
import de.nodelab.dynmc.network.packet.server.*;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

import java.io.File;
import java.util.HashMap;

public class Main {

    private NetServer netServer;
    private NetServer webDataServer;
    private NetJsonServer netJsonServer;

    @Getter
    private static Main instance;

    @Getter
    private DatabaseConnection database;

    @Getter
    private HashMap<String, PendingData> pendingData = new HashMap<>();

    @Getter
    private ConnectionStorage daemons = new ConnectionStorage();

    private Main() {
        instance = this;

        this.createFolders();
        //this.database = new DatabaseConnection();

        this.netServer = NetServer.getInstance(1337);
        this.netServer.getOutPacketRegistry()
                .add(0x01, PacketServerStart.class)
                .add(0x02, PacketServerStop.class)
                .add(0x03, PacketAuthResult.class)
                .add(0x04, PacketExceptionResult.class)
                .add(0x05, PacketStatusResult.class);
        this.netServer.getInPacketRegistry()
                .add(0x01, PacketServerStartResult.class)
                .add(0x02, PacketServerStopResult.class)
                .add(0x03, PacketAuth.class)
                .add(0x04, PacketException.class)
                .add(0x05, PacketStatus.class);

        this.netJsonServer = NetJsonServer.getInstance();
        this.netJsonServer.getInPacketRegistry()
                .add(0x01, PacketInAuth.class)
                .add(0x02, PacketInPluginUpload.class)
                .add(0x03, PacketInPluginRemove.class)
                .add(0x04, PacketInWorldUpload.class)
                .add(0x05, PacketInWorldUpdate.class)
                .add(0x06, PacketInDaemonAdd.class)
                .add(0x07, PacketInDaemonUpdate.class)
                .add(0x08, PacketInServerTypeAdd.class)
                .add(0x09, PacketInServerTypeUpdate.class)
                .add(0x10, PacketInStats.class)
                .add(0x12, PacketInWorldRemove.class)
                .add(0x13, PacketInDaemonRemove.class)
                .add(0x14, PacketInServerTypeRemove.class)
                .add(0x15, PacketInServerStart.class);
        this.netJsonServer.getOutPacketRegistry()
                .add(0x01, PacketOutAuth.class)
                .add(0x02, PacketOutPluginUpload.class)
                .add(0x03, PacketOutPluginRemove.class)
                .add(0x04, PacketOutWorldUpload.class)
                .add(0x05, PacketOutWorldUpdate.class)
                .add(0x06, PacketOutDaemonAdd.class)
                .add(0x07, PacketOutDaemonUpdate.class)
                .add(0x08, PacketOutServerTypeAdd.class)
                .add(0x09, PacketOutServerTypeUpdate.class)
                .add(0x10, PacketOutStats.class)
                .add(0x11, PacketOutDatabaseData.class)
                .add(0x12, PacketOutWorldRemove.class)
                .add(0x13, PacketOutDaemonRemove.class)
                .add(0x14, PacketOutServerTypeRemove.class)
                .add(0x15, PacketOutServerStart.class);

        this.webDataServer = NetServer.getInstance(1339);
        this.webDataServer.getInPacketRegistry().add(0x01, PacketRawData.class);

        this.netServer.setClose(() -> System.out.println("Net Server Closed"));
        this.netJsonServer.setClose(() ->  System.out.println("JSON Server closed"));
        this.netJsonServer.setClose(() ->  System.out.println("Web Data Server closed"));

        this.netServer.setClientDisconnected((ctx) -> {
            System.out.println("Daemon disconnected");
            this.daemons.values().remove(ctx);
        });

        this.netServer.getListenerRegistry().register(new PacketExceptionListener());
        this.netServer.getListenerRegistry().register(new PacketAuthListener());
        this.netServer.getListenerRegistry().register(new PacketStatusListener());

        ListenerRegistry<JsonPacket> registry = this.netJsonServer.getListenerRegistry();
        registry.register(new PacketAuthenticateListener());
        registry.register(new PacketPluginUploadListener());
        registry.register(new PacketDaemonAddListener());
        registry.register(new PacketDaemonUpdateListener());
        registry.register(new PacketPluginRemoveListener());
        registry.register(new PacketServerTypeAddListener());
        registry.register(new PacketServerTypeUpdateListener());
        registry.register(new PacketWorldUploadListener());
        registry.register(new PacketDaemonRemoveListener());
        registry.register(new PacketServerTypeRemoveListener());
        registry.register(new PacketWorldUpdateListener());
        registry.register(new PacketWorldRemoveListener());

        this.webDataServer.getListenerRegistry().register(new PacketRawDataListener());

        System.out.println("Starting servers...");
        try {
            this.netJsonServer.start();
            this.webDataServer.start();
            this.netServer.start().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createFolders() {
        File pluginsFolder = new File("files/plugins");
        File worldsFolder = new File("files/worlds");

        if(!pluginsFolder.exists()) pluginsFolder.mkdirs();
        if(!worldsFolder.exists()) worldsFolder.mkdirs();
    }

    public static void main(String[] args) {
        new Main();
    }

}
