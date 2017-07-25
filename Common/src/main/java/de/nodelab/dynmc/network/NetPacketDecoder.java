package de.nodelab.dynmc.network;

import de.nodelab.dynmc.network.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class NetPacketDecoder extends ByteToMessageDecoder {

    private final NetComponent<Packet> component;

    public NetPacketDecoder(NetComponent<Packet> component) {
        this.component = component;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        try (ByteBufInputStream is = new ByteBufInputStream(byteBuf)) {
            int id = is.readInt();
            if (this.component.getInPacketRegistry().exists(id)) {
                Packet packet = this.component.getInPacketRegistry().createPacket(id);
                packet.readFrom(is);
                list.add(packet);
            }
        }
    }

}
