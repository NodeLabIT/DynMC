package de.nodelab.dynmc.network.json.packet.in;

import de.nodelab.dynmc.network.json.packet.JsonPacket;
import lombok.Data;

@Data
public class PacketInPluginUpload extends JsonPacket {

    private String name;

}
