package de.nodelab.dynmc.network.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.nodelab.dynmc.network.json.packet.JsonPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class NetJsonDecoder extends ByteToMessageDecoder {

    private Gson gson;
    private JsonParser jsonParser;

    private NetJsonServer server;

    public NetJsonDecoder(NetJsonServer server) {
        this.server = server;
        this.gson = new Gson();
        this.jsonParser = new JsonParser();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        String text = new String(byteBuf.array());
        JsonObject o = this.jsonParser.parse(text).getAsJsonObject();

        Class<? extends JsonPacket> clazz = this.server.getPacketRegistry().getPacketById(o.get("id").getAsInt());
        if(clazz != null) {
            o.remove("id");

            JsonPacket packet = this.gson.fromJson(o, clazz);
            list.add(packet);
        }
    }

}
