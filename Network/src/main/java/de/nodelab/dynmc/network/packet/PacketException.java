package de.nodelab.dynmc.network.packet;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import lombok.Data;

import java.io.IOException;

@Data
public class PacketException implements Packet {

    private String source;
    private String stacktrace;

    @Override
    public void readFrom(ByteBufInputStream stream) throws IOException {
        this.source = stream.readUTF();
        this.stacktrace = stream.readUTF();
    }

    @Override
    public void writeTo(ByteBufOutputStream stream) throws IOException {
        stream.writeUTF(this.source);
        stream.writeUTF(this.stacktrace);
    }

}
