package de.nodelab.dynmc.network.packet;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public class PacketStatus implements Packet {

    private int cpuUsage;
    private int ramUsage;

    @Override
    public void readFrom(ByteBufInputStream stream) throws IOException {
        this.cpuUsage = stream.readInt();
        this.ramUsage = stream.readInt();
    }

    @Override
    public void writeTo(ByteBufOutputStream stream) throws IOException {
        stream.writeInt(this.cpuUsage);
        stream.writeInt(this.ramUsage);
    }

}
