package de.nodelab.dynmc.network.json.packet.in;

import lombok.Data;

@Data
public class PacketInDaemonUpdate extends PacketInDaemonAdd {

    private String newName;

}
