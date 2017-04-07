package de.nodelab.dynmc.network;

import de.nodelab.dynmc.network.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class NetPacketDecoder extends ByteToMessageDecoder {

    private final NetComponent component;

    public NetPacketDecoder(NetComponent component) {
        this.component = component;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        try (ByteBufInputStream is = new ByteBufInputStream(byteBuf)) {
            int id = is.readInt();
            System.out.println("Packet arrived: " + id);
            if (this.component.getPacketRegistry().exists(id)) {
                Packet packet = this.component.getPacketRegistry().createPacket(id);
                packet.readFrom(is);
                list.add(packet);
            }
        }
    }

}
