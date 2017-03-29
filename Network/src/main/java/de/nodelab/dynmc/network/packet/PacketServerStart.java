package de.nodelab.dynmc.network.packet;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import lombok.Data;

import java.io.IOException;

@Data
public class PacketServerStart implements Packet {

    private String servertype;

    @Override
    public void readFrom(ByteBufInputStream stream) throws IOException {
        this.servertype = stream.readUTF();
    }

    @Override
    public void writeTo(ByteBufOutputStream stream) throws IOException {
        stream.writeUTF(this.servertype);
    }

}
