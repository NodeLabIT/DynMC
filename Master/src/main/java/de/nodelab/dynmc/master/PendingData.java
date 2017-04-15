package de.nodelab.dynmc.master;

import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PendingData {

    private String type;
    private ChannelHandlerContext sender;

}
