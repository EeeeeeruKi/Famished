package com.example.famished;

import java.util.UUID;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod("famished")
public class Famished {
    private static final UUID REACH_ID = UUID.fromString("5b1f3c9e-7a64-4d2a-9e11-3c0a8f2d6b47");
    private static final UUID ATTACK_ID = UUID.fromString("a3e7c2d1-4b8f-4e6a-9c35-71d0f8b2e914");
    private static final UUID SPEED_ID = UUID.fromString("c9d4b7e2-1f3a-4c8d-b6e5-28a1f0d3c765");

    public Famished() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.side != LogicalSide.SERVER) {
            return;
        }
        Player player = event.player;
        boolean exempt = player.isCreative() || player.isSpectator();
        int food = player.getFoodData().getFoodLevel();
        float health = player.getHealth();

        // 空腹時のリーチ低下: 満腹度6以下で -0.5、2以下で -1.0
        double reach = 0.0;
        if (!exempt) {
            if (food <= 2) {
                reach = -1.0;
            } else if (food <= 6) {
                reach = -0.5;
            }
        }
        apply(player, ForgeMod.ENTITY_REACH.get(), REACH_ID, "Famished reach penalty",
                reach, AttributeModifier.Operation.ADDITION);

        // 空腹時の攻撃力低下: 満腹度6以下で -4（弱体化 I と同じ量）
        double attack = (!exempt && food <= 6) ? -4.0 : 0.0;
        apply(player, Attributes.ATTACK_DAMAGE, ATTACK_ID, "Famished attack penalty",
                attack, AttributeModifier.Operation.ADDITION);

        // 低HP時の移動速度低下: HP10以下で -10%、6以下で -25%、4以下で -40%
        double speed = 0.0;
        if (!exempt) {
            if (health <= 4.0F) {
                speed = -0.40;
            } else if (health <= 6.0F) {
                speed = -0.25;
            } else if (health <= 10.0F) {
                speed = -0.10;
            }
        }
        apply(player, Attributes.MOVEMENT_SPEED, SPEED_ID, "Famished speed penalty",
                speed, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    private static void apply(Player player, Attribute attribute, UUID id, String name,
            double amount, AttributeModifier.Operation operation) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance == null) {
            return;
        }
        AttributeModifier current = instance.getModifier(id);
        if (current != null && current.getAmount() == amount) {
            return;
        }
        if (current != null) {
            instance.removeModifier(id);
        }
        if (amount != 0.0) {
            instance.addTransientModifier(new AttributeModifier(id, name, amount, operation));
        }
    }
}
