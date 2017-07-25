package de.nodelab.dynmc.network.json.packet.out;

import de.nodelab.dynmc.network.json.packet.JsonPacket;
import lombok.Data;

@Data
public class PacketOutOk extends JsonPacket {

    private boolean ok = true;

}
