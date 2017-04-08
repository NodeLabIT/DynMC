package de.nodelab.dynmc.network.json.packet;

import lombok.Getter;
import lombok.Setter;

public class PacketAuthenticate extends JsonPacket {

    @Getter @Setter
    private String key;

}
