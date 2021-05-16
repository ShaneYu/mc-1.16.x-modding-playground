package com.github.shaneyu.playground.client;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.common.config.PlaygroundConfig;
import com.github.shaneyu.playground.common.item.interfaces.IModeItem;
import com.github.shaneyu.playground.common.network.packet.PacketModeChange;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientTickHandler {
    public static final Minecraft minecraft = Minecraft.getInstance();

    private static long lastScrollTime = -1;
    private static double scrollDelta;

    @SubscribeEvent
    public void onTick(ClientTickEvent event) {
        if (event.phase == Phase.START) {
            tickStart();
        }
    }

    public void tickStart() {
        if (minecraft.world != null && minecraft.player != null && !minecraft.isGamePaused()) {

            if (minecraft.world.getGameTime() - lastScrollTime > 20) {
                scrollDelta = 0;
            }
        }
    }

    @SubscribeEvent
    public void onMouseEvent(InputEvent.MouseScrollEvent event) {
        if (PlaygroundConfig.client.allowModeScroll.get() && minecraft.player != null && minecraft.player.isSneaking()) {
            handleModeScroll(event, event.getScrollDelta());
        }
    }

    private void handleModeScroll(Event event, double delta) {
        if (delta != 0 && IModeItem.isModeItem(minecraft.player, EquipmentSlotType.MAINHAND)) {
            lastScrollTime = minecraft.world.getGameTime();
            scrollDelta += delta;

            int shift = (int) scrollDelta;
            scrollDelta %= 1;

            if (shift != 0) {
                Playground.packetHandler.sendToServer(new PacketModeChange(EquipmentSlotType.MAINHAND, shift, true));
            }

            event.setCanceled(true);
        }
    }
}
