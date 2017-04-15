package de.nodelab.dynmc.network.json.packet.in;

import lombok.Data;

@Data
public class PacketInWorldUpdate extends PacketInWorldUpload {

    private String newName;

}
