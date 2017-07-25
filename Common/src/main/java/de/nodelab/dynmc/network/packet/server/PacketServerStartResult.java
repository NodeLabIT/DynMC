package de.nodelab.dynmc.network.packet.server;

import de.nodelab.dynmc.network.packet.PacketOk;
import lombok.Data;

@Data
public class PacketServerStartResult extends PacketOk {

    private String name;

}
