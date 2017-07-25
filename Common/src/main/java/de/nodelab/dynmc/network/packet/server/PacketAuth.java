package de.nodelab.dynmc.network.packet.server;

import de.nodelab.dynmc.network.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import lombok.Data;

import java.io.IOException;

@Data
public class PacketAuth implements Packet {

    private String key;

    @Override
    public void readFrom(ByteBufInputStream stream) throws IOException {
        this.key = stream.readUTF();
    }

    @Override
    public void writeTo(ByteBufOutputStream stream) throws IOException {
        stream.writeUTF(this.key);
    }

}
