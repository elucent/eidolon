package elucent.eidolon.item;

import java.util.Random;

import elucent.eidolon.Registry;
import elucent.eidolon.entity.SoulfireProjectileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.server.level.ServerLevel;

import net.minecraft.world.item.Item.Properties;

public class SoulfireWandItem extends WandItem {
	static Random random = new Random();
	
    public SoulfireWandItem(Properties builderIn) {
        super(builderIn);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        ItemStack stack = entity.getItemInHand(hand);
        if (!entity.swinging) {
            if (!world.isClientSide) {
                Vec3 pos = entity.position().add(entity.getLookAngle().scale(0.5)).add(0.5 * Math.sin(Math.toRadians(225 - entity.yHeadRot)), entity.getBbHeight() * 2 / 3, 0.5 * Math.cos(Math.toRadians(225 - entity.yHeadRot)));
                Vec3 vel = entity.getEyePosition(0).add(entity.getLookAngle().scale(40)).subtract(pos).scale(1.0 / 20);
                world.addFreshEntity(new SoulfireProjectileEntity(Registry.SOULFIRE_PROJECTILE.get(), world).shoot(
                    pos.x, pos.y, pos.z, vel.x, vel.y, vel.z, entity.getUUID()
                ));
                world.playSound(null, pos.x, pos.y, pos.z, Registry.CAST_SOULFIRE_EVENT.get(), SoundSource.NEUTRAL, 0.75f, random.nextFloat() * 0.2f + 0.9f);
                stack.hurtAndBreak(1, entity, (player) -> {
                    player.broadcastBreakEvent(hand);
                });
            }
            entity.swing(hand);
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }
}
