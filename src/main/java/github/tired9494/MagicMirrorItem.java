package github.tired9494;

import github.tired9494.config.ModConfig;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import java.util.List;
import java.util.Optional;


public class MagicMirrorItem extends Item {

    public MagicMirrorItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(new TranslatableText("magic_mirror.tooltip"));
    }


    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {

        if (!player.world.isClient()) {
            if (player.experienceLevel >= ModConfig.INSTANCE.xpCost || player.abilities.creativeMode) {

                ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
                BlockPos spawnpoint = new BlockPos(serverPlayer.getSpawnPointPosition());
                Optional<Vec3d> optionalSpawnVec = player.findRespawnPosition((ServerWorld)world, spawnpoint, serverPlayer.getSpawnAngle(),false,false);

                optionalSpawnVec.ifPresent(spawnVec -> {
                    serverPlayer.setExperienceLevel(player.experienceLevel - ModConfig.INSTANCE.xpCost);
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, ModConfig.INSTANCE.blindnessLength, 0));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, ModConfig.INSTANCE.weaknessLength, 0));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, ModConfig.INSTANCE.fatigueLength, 2));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, ModConfig.INSTANCE.hungerLength, 0));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, ModConfig.INSTANCE.nauseaLength, 0));
                    serverPlayer.teleport(spawnVec.getX(), spawnVec.getY(), spawnVec.getZ());
                    world.playSound(null, spawnpoint, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.BLOCKS, 0.4f, 1f);
                    serverPlayer.damage(DamageSource.magic(player, player), ModConfig.INSTANCE.mirrorDamage);
                });
                if (!optionalSpawnVec.isPresent()) {
                    player.sendSystemMessage(new TranslatableText("magic_mirror.fail"), Util.NIL_UUID);
                    world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_SHULKER_BULLET_HURT, SoundCategory.BLOCKS, 1f, 1f);
                }
            }
            else {
                player.sendSystemMessage(new TranslatableText("magic_mirror.fail"), Util.NIL_UUID);
                world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_SHULKER_BULLET_HURT, SoundCategory.BLOCKS, 1f, 1f);
            }
        }
        player.getItemCooldownManager().set(this, 160);
        return new TypedActionResult<>(ActionResult.SUCCESS, player.getStackInHand(hand));
    }
}