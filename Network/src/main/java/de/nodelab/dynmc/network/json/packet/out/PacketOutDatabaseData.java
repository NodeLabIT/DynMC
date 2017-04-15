package de.nodelab.dynmc.network.json.packet.out;

import de.nodelab.dynmc.network.json.packet.JsonPacket;
import lombok.Data;

@Data
public class PacketOutDatabaseData<T> extends JsonPacket {

    private String type;
    private T data;

}
