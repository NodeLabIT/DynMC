package de.nodelab.dynmc.network.json.packet.in;

import de.nodelab.dynmc.network.json.packet.JsonPacket;
import lombok.Data;

@Data
public class PacketInDaemonAdd extends JsonPacket {

    private String name;
    private String host;
    private int minPort;
    private int maxPort;

}
