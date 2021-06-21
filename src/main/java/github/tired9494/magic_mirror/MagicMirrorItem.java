package github.tired9494.magic_mirror;

import net.minecraft.client.item.TooltipContext;
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

import static github.tired9494.magic_mirror.MagicMirror.getConfig;


public class MagicMirrorItem extends Item {

    public MagicMirrorItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(new TranslatableText("magic_mirror.tooltip"));
    }


    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {

        if (!world.isClient()) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
            ServerWorld serverWorld = serverPlayer.server.getWorld(serverPlayer.getSpawnPointDimension());
            if ((player.experienceLevel >= getConfig().xpCost || player.getAbilities().creativeMode) && serverWorld != null && (getConfig().interdimensional || serverWorld == serverPlayer.getServerWorld())) {


                BlockPos spawnpoint = new BlockPos(serverPlayer.getSpawnPointPosition());
                Optional<Vec3d> optionalSpawnVec = player.findRespawnPosition(serverWorld, spawnpoint, serverPlayer.getSpawnAngle(),false,false);

                optionalSpawnVec.ifPresent(spawnVec -> {
                    serverPlayer.setExperienceLevel(player.experienceLevel - getConfig().xpCost);
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, getConfig().blindnessLength, 0));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, getConfig().weaknessLength, 0));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, getConfig().fatigueLength, 2));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, getConfig().hungerLength, 0));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, getConfig().nauseaLength, 0));
                    serverPlayer.teleport(serverWorld, spawnVec.getX(), spawnVec.getY(), spawnVec.getZ(),serverPlayer.getSpawnAngle(), 0.5F);
                    serverWorld.playSound(null, spawnpoint, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.BLOCKS, 0.4f, 1f);
                    serverPlayer.damage(DamageSource.magic(player, player), getConfig().damage);
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