package github.tired9494.magic_mirror;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static github.tired9494.magic_mirror.MagicMirror.getConfig;


public class MagicMirrorItem extends Item {

    public MagicMirrorItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(new TranslatableText("magic_mirror.tooltip"));
    }


    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }


    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity player = (PlayerEntity)user;
        if (!world.isClient()) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
            ServerWorld serverWorld = serverPlayer.server.getWorld(serverPlayer.getSpawnPointDimension());
            if (player.experienceLevel >= getConfig().xpCost || player.getAbilities().creativeMode) {
                if (serverWorld != null && (getConfig().interdimensional || serverWorld == serverPlayer.getServerWorld())) {
                    int weaknessLevel = getConfig().effectOptions.weaknessLevel - 1;
                    int fatigueLevel = getConfig().effectOptions.fatigueLevel - 1;
                    int blindnessLevel = getConfig().effectOptions.blindnessLevel - 1;
                    int hungerLevel = getConfig().effectOptions.hungerLevel - 1;
                    int nauseaLevel = getConfig().effectOptions.nauseaLevel - 1;

                    int weaknessLength = getConfig().effectOptions.weaknessLength;
                    int fatigueLength = getConfig().effectOptions.fatigueLength;
                    int blindnessLength = getConfig().effectOptions.blindnessLength;
                    int hungerLength = getConfig().effectOptions.hungerLength;
                    int nauseaLength = getConfig().effectOptions.nauseaLength;

                    BlockPos spawnpoint = serverPlayer.getSpawnPointPosition();
                    Optional<Vec3d> optionalSpawnVec = player.findRespawnPosition(serverWorld, spawnpoint, serverPlayer.getSpawnAngle(), false, false);
                    BlockPos finalSpawnpoint = spawnpoint;

                    //Player spawn
                    optionalSpawnVec.ifPresent(spawnVec -> {
                        mirrorEffects(player, serverPlayer, weaknessLevel, fatigueLevel, blindnessLevel, hungerLevel, nauseaLevel, weaknessLength, fatigueLength, blindnessLength, hungerLength, nauseaLength);
                        serverPlayer.teleport(serverWorld, spawnVec.getX(), spawnVec.getY(), spawnVec.getZ(), serverPlayer.getSpawnAngle(), 0.5F);
                        serverWorld.playSound(null, finalSpawnpoint, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 0.4f, 1f);
                    });

                    // World Spawn
                    if (!optionalSpawnVec.isPresent()) {
                        if (!getConfig().spawnSet) {
                            spawnpoint = serverWorld.getSpawnPos();
                            serverPlayer.teleport(serverWorld, spawnpoint.getX(), spawnpoint.getY(), spawnpoint.getZ(), serverPlayer.getSpawnAngle(), 0.5F);
                            while (!serverWorld.isSpaceEmpty(serverPlayer)) {
                                serverPlayer.teleport(serverPlayer.getX(), serverPlayer.getY() + 1.0D, serverPlayer.getZ());
                            }
                            mirrorEffects(player, serverPlayer, weaknessLevel, fatigueLevel, blindnessLevel, hungerLevel, nauseaLevel, weaknessLength, fatigueLength, blindnessLength, hungerLength, nauseaLength);
                            serverWorld.playSound(null, spawnpoint, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 0.4f, 1f);
                        } else {
                            player.sendSystemMessage(new TranslatableText("magic_mirror.fail"), Util.NIL_UUID);
                            world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_SHULKER_BULLET_HURT, SoundCategory.BLOCKS, 1f, 1f);
                        }
                    }
                } else {
                    player.sendSystemMessage(new TranslatableText("magic_mirror.fail"), Util.NIL_UUID);
                    world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_SHULKER_BULLET_HURT, SoundCategory.BLOCKS, 1f, 1f);
                }
            }
            else {
                player.sendSystemMessage(new TranslatableText("magic_mirror.xpfail"), Util.NIL_UUID);
                world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_SHULKER_BULLET_HURT, SoundCategory.BLOCKS, 1f, 1f);
            }
        }
        if (player != null) {
            player.getItemCooldownManager().set(this, 160);
            player.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        return stack;
    }

    private void mirrorEffects(PlayerEntity player, ServerPlayerEntity serverPlayer, int weaknessLevel, int fatigueLevel, int blindnessLevel, int hungerLevel, int nauseaLevel, int weaknessLength, int fatigueLength, int blindnessLength, int hungerLength, int nauseaLength) {
        if (!player.getAbilities().creativeMode) {
            serverPlayer.setExperienceLevel(player.experienceLevel - getConfig().xpCost);
            serverPlayer.damage(DamageSource.magic(player, player), getConfig().damage);
        }
        if (weaknessLevel > -1 && weaknessLength > 0) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, getConfig().effectOptions.weaknessLength, weaknessLevel));
        }
        if (fatigueLevel > -1 && fatigueLength > 0) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, getConfig().effectOptions.fatigueLength, fatigueLevel));
        }
        if (blindnessLevel > -1 && blindnessLength > 0) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, getConfig().effectOptions.blindnessLength, blindnessLevel));
        }
        if (hungerLevel > -1 && hungerLength > 0) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, getConfig().effectOptions.hungerLength, hungerLevel));
        }
        if (nauseaLevel > -1 && nauseaLength > 0) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, getConfig().effectOptions.nauseaLength, nauseaLevel));
        }
    }

    private final int useTime = getConfig().useTime > 0 ? getConfig().useTime : 1;

    public int getMaxUseTime(ItemStack stack) {
        return useTime;
    }
}