package de.nodelab.dynmc.network.json.packet.in;

import de.nodelab.dynmc.network.json.packet.JsonPacket;
import lombok.Data;

@Data
public class PacketInWorldUpload extends JsonPacket {

    private String name;
    private String description;

}
