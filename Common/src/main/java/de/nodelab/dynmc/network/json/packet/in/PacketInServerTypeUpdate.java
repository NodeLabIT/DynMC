package de.nodelab.dynmc.network.json.packet.in;

import lombok.Data;

@Data
public class PacketInServerTypeUpdate extends PacketInServerTypeAdd {

    private String newName;

}
