package de.nodelab.dynmc.network.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.nodelab.dynmc.network.json.packet.JsonPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NetJsonEncoder extends MessageToByteEncoder<JsonPacket> {

    private Gson gson;
    private NetJsonServer server;

    public NetJsonEncoder(NetJsonServer server) {
        this.gson = new Gson();
        this.server = server;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, JsonPacket jsonPacket, ByteBuf byteBuf) throws Exception {
        int id = this.server.getOutPacketRegistry().getIdByPacket(jsonPacket.getClass());
        String json = this.gson.toJson(jsonPacket);
        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        obj.addProperty("id", id);
        byteBuf.writeBytes(this.gson.toJson(obj).getBytes());
    }

}
