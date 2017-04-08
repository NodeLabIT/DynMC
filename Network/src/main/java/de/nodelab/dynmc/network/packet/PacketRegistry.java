package de.nodelab.dynmc.network.packet;

import com.google.common.collect.HashBiMap;

import java.lang.reflect.InvocationTargetException;

public class PacketRegistry<T> {

    private HashBiMap<Integer, Class<? extends T>> packetClasses;

    public PacketRegistry() {
        this.packetClasses = HashBiMap.create();
    }

    public PacketRegistry add(int id, Class<? extends T> clazz) {
        this.packetClasses.put(id, clazz);
        return this;
    }

    public int getIdByPacket(Class<? extends Packet> clazz) {
        return this.packetClasses.inverse().get(clazz);
    }

    public Class<? extends T> getPacketById(int id) {
        return this.packetClasses.get(id);
    }

    public T createPacket(int id) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<? extends T> clazz = this.getPacketById(id);
        if(clazz != null) {
            return (T) clazz.getConstructors()[0].newInstance();
        }
        return null;
    }

    public boolean exists(int id) {
        return this.packetClasses.containsKey(id);
    }

}
