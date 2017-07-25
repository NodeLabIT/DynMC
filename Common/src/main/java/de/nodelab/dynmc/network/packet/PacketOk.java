package de.nodelab.dynmc.network.packet;

import de.nodelab.dynmc.network.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public class PacketOk implements Packet {

    private boolean ok;

    @Override
    public void readFrom(ByteBufInputStream stream) throws IOException {
        this.ok = stream.readBoolean();
    }

    @Override
    public void writeTo(ByteBufOutputStream stream) throws IOException {
        stream.writeBoolean(this.ok);
    }
}
