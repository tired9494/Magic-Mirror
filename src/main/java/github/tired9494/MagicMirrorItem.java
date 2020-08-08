package github.tired9494;

import github.tired9494.config.ModConfig;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.List;



public class MagicMirrorItem extends Item {

    public MagicMirrorItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(new TranslatableText("magic_mirror.tooltip"));
    }


    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        boolean bl = false;
        if (!playerEntity.world.isClient()) {

            playerEntity.getItemCooldownManager().set(this, 200);
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) playerEntity;
            BlockPos spawnpoint = serverPlayer.getSpawnPointPosition();



            if (spawnpoint != null && serverPlayer.getSpawnPointDimension().getValue()
                    .equals(world.getRegistryKey().getValue())) {

                playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, ModConfig.INSTANCE.blindnessLength, 0));
                playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, ModConfig.INSTANCE.weaknessLength, 0));
                playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, ModConfig.INSTANCE.fatigueLength, 2));
                playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, ModConfig.INSTANCE.hungerLength, 0));
                playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, ModConfig.INSTANCE.nauseaLength, 0));

                serverPlayer.teleport(spawnpoint.getX(), spawnpoint.getY(), spawnpoint.getZ());
                world.playSound(null, spawnpoint, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.BLOCKS, 0.4f, 1f);
                serverPlayer.damage(DamageSource.magic(playerEntity, playerEntity), ModConfig.INSTANCE.mirrorDamage);
                spawnpoint = null;

            } else {
                playerEntity.sendSystemMessage(new TranslatableText("magic_mirror.fail"), Util.NIL_UUID);
                world.playSound(null, playerEntity.getBlockPos(), SoundEvents.ENTITY_SHULKER_BULLET_HURT, SoundCategory.BLOCKS, 1f, 1f);
            }
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, playerEntity.getStackInHand(hand));
    }
}