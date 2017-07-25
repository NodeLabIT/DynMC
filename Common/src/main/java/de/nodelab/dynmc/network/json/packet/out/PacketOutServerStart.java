package de.nodelab.dynmc.network.json.packet.out;

import de.nodelab.dynmc.network.json.packet.JsonPacket;
import lombok.Data;

@Data
public class PacketOutServerStart extends JsonPacket {

    private String daemon;
    private String servertype;
    private String ip;

}
