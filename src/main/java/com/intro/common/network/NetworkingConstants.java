package com.intro.common.network;

import net.minecraft.resources.ResourceLocation;

public class NetworkingConstants {

    // be sure to contain all set options in this packet
    public static final ResourceLocation SET_SETTING_PACKET_ID = new ResourceLocation("osmium", "set_setting_packet");
    public static final ResourceLocation RUNNING_OSMIUM_SERVER_PACKET_ID = new ResourceLocation("osmium", "running_server_packet");
    public static final ResourceLocation RUNNING_OSMIUM_CLIENT_PACKET_ID = new ResourceLocation("osmium", "running_osmium_client");
    public static final ResourceLocation SET_PLAYER_CAPE_SERVER_BOUND = new ResourceLocation("osmium", "set_player_cape_server_bound");
    public static final ResourceLocation SET_PLAYER_CAPE_CLIENT_BOUND = new ResourceLocation("osmium", "set_player_cape_client_bound");



}
