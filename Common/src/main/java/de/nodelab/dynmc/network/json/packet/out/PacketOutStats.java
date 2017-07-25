package de.nodelab.dynmc.network.json.packet.out;

import de.nodelab.dynmc.network.json.packet.JsonPacket;
import lombok.Data;

@Data
public class PacketOutStats extends JsonPacket {

    private Object data; // TODO: Specify data class

}
