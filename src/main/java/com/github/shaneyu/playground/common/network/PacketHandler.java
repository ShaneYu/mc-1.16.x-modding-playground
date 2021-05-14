package com.github.shaneyu.playground.common.network;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.common.network.packet.PacketUpdateTile;
import com.github.shaneyu.playground.lib.network.BasePacketHandler;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler extends BasePacketHandler {
    private static final SimpleChannel netHandler = createChannel(Playground.modLoc(Playground.MOD_ID), PacketHandler::getProtocolVersion);

    @Override
    protected SimpleChannel getChannel() {
        return netHandler;
    }

    @Override
    public void initialize() {
        // Client to server messages
        // TODO: Register any client to server messages here

        // Server to client messages
        registerServerToClient(PacketUpdateTile.class, PacketUpdateTile::encode, PacketUpdateTile::decode, PacketUpdateTile::handle);
    }

    private static String getProtocolVersion() {
        return Playground.instance == null ? "999.999.999" : Playground.getVersion();
    }
}
