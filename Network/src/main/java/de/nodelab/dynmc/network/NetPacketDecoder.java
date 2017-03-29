package de.nodelab.dynmc.network;

import de.nodelab.dynmc.network.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class NetPacketDecoder extends ByteToMessageDecoder {

    private final NetServer server;

    public NetPacketDecoder() {
        this.server = NetServer.getInstance();
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        try (ByteBufInputStream is = new ByteBufInputStream(byteBuf)) {
            int id = is.readInt();
            System.out.println("Packet arrived: " + id);
            if (this.server.getPacketRegistry().exists(id)) {
                Packet packet = this.server.getPacketRegistry().createPacket(id);
                packet.readFrom(is);
                list.add(packet);
            }
        }
    }

}
