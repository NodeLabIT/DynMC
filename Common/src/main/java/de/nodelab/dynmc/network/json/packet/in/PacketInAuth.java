package de.nodelab.dynmc.network.json.packet.in;

import de.nodelab.dynmc.network.json.packet.JsonPacket;
import lombok.Data;

@Data
public class PacketInAuth extends JsonPacket {

    private String key;

}
