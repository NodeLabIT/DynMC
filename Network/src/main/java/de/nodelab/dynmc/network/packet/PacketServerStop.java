package de.nodelab.dynmc.network.packet;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public class PacketServerStop implements Packet {

    private String id;

    @Override
    public void readFrom(ByteBufInputStream stream) throws IOException {
        this.id = stream.readUTF();
    }

    @Override
    public void writeTo(ByteBufOutputStream stream) throws IOException {
        stream.writeUTF(this.id);
    }

}
