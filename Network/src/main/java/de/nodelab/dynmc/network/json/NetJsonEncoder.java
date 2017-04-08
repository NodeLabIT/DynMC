package de.nodelab.dynmc.network.json;

import com.google.gson.Gson;
import de.nodelab.dynmc.network.json.packet.JsonPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NetJsonEncoder extends MessageToByteEncoder<JsonPacket> {

    private Gson gson;

    public NetJsonEncoder() {
        this.gson = new Gson();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, JsonPacket jsonPacket, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(this.gson.toJson(jsonPacket).getBytes());
    }

}
