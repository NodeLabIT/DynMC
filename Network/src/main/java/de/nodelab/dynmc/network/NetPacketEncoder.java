package de.nodelab.dynmc.network;

import de.nodelab.dynmc.network.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NetPacketEncoder extends MessageToByteEncoder<Packet> {

    private final NetServer server;

    public NetPacketEncoder() {
        this.server = NetServer.getInstance();
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        System.out.println("Encoded packet: " + this.server.getPacketRegistry().getIdByPacket(packet.getClass()));
        try (ByteBufOutputStream os = new ByteBufOutputStream(byteBuf)) {
            os.writeInt(this.server.getPacketRegistry().getIdByPacket(packet.getClass()));
            packet.writeTo(os);
        }
    }

}
