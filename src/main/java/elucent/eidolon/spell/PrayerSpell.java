package elucent.eidolon.spell;

import elucent.eidolon.Registry;
import elucent.eidolon.block.HorizontalBlockBase;
import elucent.eidolon.capability.KnowledgeProvider;
import elucent.eidolon.capability.ReputationProvider;
import elucent.eidolon.particle.Particles;
import elucent.eidolon.ritual.Ritual;
import elucent.eidolon.tile.EffigyTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.List;

public class PrayerSpell extends StaticSpell {
    ResourceLocation deity;

    public PrayerSpell(ResourceLocation name, ResourceLocation deity, Sign... signs) {
        super(name, signs);
        this.deity = deity;
    }

    @Override
    public boolean canCast(World world, BlockPos pos, PlayerEntity player) {
        List<EffigyTileEntity> effigies = Ritual.getTilesWithinAABB(EffigyTileEntity.class, world, new AxisAlignedBB(pos.add(-4, -4, -4), pos.add(5, 5, 5)));
        if (effigies.size() == 0) return false;
        EffigyTileEntity effigy = effigies.stream().min(Comparator.comparingDouble((e) -> e.getPos().distanceSq(pos))).get();
        return effigy.ready();
    }

    @Override
    public void cast(World world, BlockPos pos, PlayerEntity player) {
        List<EffigyTileEntity> effigies = Ritual.getTilesWithinAABB(EffigyTileEntity.class, world, new AxisAlignedBB(pos.add(-4, -4, -4), pos.add(5, 5, 5)));
        if (effigies.size() == 0) return;
        EffigyTileEntity effigy = effigies.stream().min(Comparator.comparingDouble((e) -> e.getPos().distanceSq(pos))).get();
        if (!world.isRemote) {
            effigy.pray();
            player.getCapability(ReputationProvider.CAPABILITY, null).ifPresent((rep) -> {
                rep.addReputation(player, deity, 1.0);
            });
        }
        else {
            world.playSound(player, effigy.getPos(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.NEUTRAL, 10000.0F, 0.6F + world.rand.nextFloat() * 0.2F);
            world.playSound(player, effigy.getPos(), SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.NEUTRAL, 2.0F, 0.5F + world.rand.nextFloat() * 0.2F);
            BlockState state = world.getBlockState(effigy.getPos());
            Direction dir = state.get(HorizontalBlockBase.HORIZONTAL_FACING);
            Direction tangent = dir.rotateY();
            float x = effigy.getPos().getX() + 0.5f + dir.getXOffset() * 0.21875f;
            float y = effigy.getPos().getY() + 0.8125f;
            float z = effigy.getPos().getZ() + 0.5f + dir.getZOffset() * 0.21875f;
            Particles.create(Registry.FLAME_PARTICLE)
                .setColor(1, 0.25f, 0.0625f)
                .setAlpha(0.5f, 0)
                .setScale(0.125f, 0.0625f)
                .randomOffset(0.01f)
                .randomVelocity(0.0025f).addVelocity(0, 0.005f, 0)
                .repeat(world, x + 0.09375f * tangent.getXOffset(), y, z + 0.09375f * tangent.getZOffset(), 8);
            Particles.create(Registry.FLAME_PARTICLE)
                .setColor(1, 0.25f, 0.0625f)
                .setAlpha(0.5f, 0)
                .setScale(0.1875f, 0.125f)
                .randomOffset(0.01f)
                .randomVelocity(0.0025f).addVelocity(0, 0.005f, 0)
                .repeat(world, x - 0.09375f * tangent.getXOffset(), y, z - 0.09375f * tangent.getZOffset(), 8);
        }
    }
}
