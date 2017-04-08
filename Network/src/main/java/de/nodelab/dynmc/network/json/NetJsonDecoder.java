package de.nodelab.dynmc.network.json;

import com.google.common.primitives.Chars;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.nodelab.dynmc.network.json.packet.JsonPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

public class NetJsonDecoder extends ByteToMessageDecoder {

    private Gson gson;
    private JsonParser jsonParser;

    private NetJsonServer server;

    private final Charset UTF_8 = Charset.forName("UTF-8");

    public NetJsonDecoder(NetJsonServer server) {
        this.server = server;
        this.gson = new Gson();
        this.jsonParser = new JsonParser();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String text = new String(bytes, UTF_8);

        JsonObject o = this.jsonParser.parse(text).getAsJsonObject();

        Class<? extends JsonPacket> clazz = this.server.getPacketRegistry().getPacketById(o.get("id").getAsInt());
        if(clazz != null) {
            o.remove("id");

            JsonPacket packet = this.gson.fromJson(o, clazz);
            list.add(packet);
        }
    }

}
