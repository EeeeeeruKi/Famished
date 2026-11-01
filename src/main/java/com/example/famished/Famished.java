package com.example.famished;

import java.util.UUID;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod("famished")
public class Famished {
    private static final UUID MODIFIER_ID = UUID.fromString("5b1f3c9e-7a64-4d2a-9e11-3c0a8f2d6b47");

    public Famished() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.side != LogicalSide.SERVER) {
            return;
        }
        Player player = event.player;
        AttributeInstance reach = player.getAttribute(ForgeMod.ENTITY_REACH.get());
        if (reach == null) {
            return;
        }

        double penalty = 0.0;
        if (!player.isCreative() && !player.isSpectator()) {
            int food = player.getFoodData().getFoodLevel();
            if (food <= 2) {
                penalty = -1.0;
            } else if (food <= 6) {
                penalty = -0.5;
            }
        }

        AttributeModifier current = reach.getModifier(MODIFIER_ID);
        if (current != null && current.getAmount() == penalty) {
            return;
        }
        if (current != null) {
            reach.removeModifier(MODIFIER_ID);
        }
        if (penalty != 0.0) {
            reach.addTransientModifier(new AttributeModifier(
                    MODIFIER_ID, "Famished reach penalty", penalty, AttributeModifier.Operation.ADDITION));
        }
    }
}
