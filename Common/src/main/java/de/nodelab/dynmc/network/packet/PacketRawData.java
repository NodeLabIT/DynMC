package de.nodelab.dynmc.network.packet;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import lombok.Data;

import java.io.IOException;

@Data
public class PacketRawData implements Packet {

    private String name;
    private byte[] data;

    @Override
    public void readFrom(ByteBufInputStream stream) throws IOException {
        this.name = stream.readUTF();
        int size = stream.readInt();
        this.data = new byte[size];
        stream.read(this.data, 0, size);
    }

    @Override
    public void writeTo(ByteBufOutputStream stream) throws IOException {
        stream.writeUTF(name);
        stream.writeInt(data.length);
        stream.write(data);
    }

}
