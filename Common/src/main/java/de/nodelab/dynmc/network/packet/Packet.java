package de.nodelab.dynmc.network.packet;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public interface Packet {

    public abstract void readFrom(ByteBufInputStream stream) throws IOException;

    public abstract void writeTo(ByteBufOutputStream stream) throws IOException;

}
