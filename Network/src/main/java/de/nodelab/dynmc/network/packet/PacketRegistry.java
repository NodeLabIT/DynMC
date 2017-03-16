package de.nodelab.dynmc.network.packet;

import com.google.common.collect.HashBiMap;

import java.lang.reflect.InvocationTargetException;

public class PacketRegistry {

    private HashBiMap<Integer, Class<? extends Packet>> packetClasses;

    public PacketRegistry() {
        this.packetClasses = HashBiMap.create();
    }

    public PacketRegistry add(int id, Class<? extends Packet> clazz) {
        this.packetClasses.put(id, clazz);
        return this;
    }

    public int getIdByPacket(Class<? extends Packet> clazz) {
        return this.packetClasses.inverse().get(clazz);
    }

    public Class<? extends Packet> getPacketById(int id) {
        return this.packetClasses.get(id);
    }

    public Packet createPacket(int id) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<? extends Packet> clazz = this.getPacketById(id);
        if(clazz != null) {
            return (Packet) clazz.getConstructors()[0].newInstance();
        }
        return null;
    }

    public boolean exists(int id) {
        return this.packetClasses.containsKey(id);
    }

}
