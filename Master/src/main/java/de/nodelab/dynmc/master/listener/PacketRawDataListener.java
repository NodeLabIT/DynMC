package de.nodelab.dynmc.master.listener;

import com.esotericsoftware.yamlbeans.YamlReader;
import de.nodelab.dynmc.master.Main;
import de.nodelab.dynmc.master.db.obj.DatabasePlugin;
import de.nodelab.dynmc.network.events.PacketListen;
import de.nodelab.dynmc.network.events.PacketListener;
import de.nodelab.dynmc.network.json.packet.out.PacketOutPluginUpload;
import de.nodelab.dynmc.network.json.packet.out.PacketOutWorldUpload;
import de.nodelab.dynmc.network.packet.PacketRawData;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PacketRawDataListener extends PacketListener {

    @PacketListen
    public void onRawData(ChannelHandlerContext ctx, PacketRawData packet) {
        if(Main.getInstance().getPendingData().containsKey(packet.getName())) {
            switch (Main.getInstance().getPendingData().get(packet.getName()).getType()) {
                case "plugin":
                    File target = new File("files/plugins/" + packet.getName() + ".jar");
                    PluginYml pluginYml = null;
                    int size = packet.getData().length;
                    try {
                        target.createNewFile();
                        FileOutputStream fos = new FileOutputStream(target, false);
                        fos.write(packet.getData());
                        fos.close();
                        ZipFile zipFile = new ZipFile(target);
                        Enumeration<? extends ZipEntry> entries = zipFile.entries();

                        while(entries.hasMoreElements()) {
                            ZipEntry entry = entries.nextElement();
                            if(entry.getName().equalsIgnoreCase("plugin.yml")) {
                                InputStream inputStream = zipFile.getInputStream(entry);
                                YamlReader reader = new YamlReader(new InputStreamReader(inputStream));
                                pluginYml = reader.read(PluginYml.class);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(pluginYml != null) {
                        DatabasePlugin plugin = new DatabasePlugin();
                        plugin.setAuthor(pluginYml.getAuthor());
                        plugin.setDescription(pluginYml.getDescription());
                        plugin.setName(pluginYml.getName());
                        plugin.setSize(size);
                        plugin.setVersion(pluginYml.getVersion());
                        Main.getInstance().getDatabase().async().addPlugin(plugin, () -> {
                            ChannelHandlerContext ctxw = Main.getInstance().getPendingData().get(packet.getName()).getSender();
                            if (!ctxw.isRemoved()) {
                                ctxw.writeAndFlush(new PacketOutPluginUpload());
                            }
                        });
                    }
                    break;
                case "world":
                    File targetW = new File("files/worlds/" + packet.getName() + ".zip");
                    try {
                        targetW.createNewFile();
                        FileOutputStream fos = new FileOutputStream(targetW, false);
                        fos.write(packet.getData());
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ChannelHandlerContext ctxw = Main.getInstance().getPendingData().get(packet.getName()).getSender();
                    if(!ctxw.isRemoved()) {
                        ctxw.writeAndFlush(new PacketOutWorldUpload());
                    }
                    break;
            }
        }
    }

    @Data
    private class PluginYml {

        private String name;
        private String author;
        private String description;
        private String version;

    }

}
