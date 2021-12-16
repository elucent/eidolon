package elucent.eidolon.spell;

import java.util.Comparator;
import java.util.List;

import elucent.eidolon.Registry;
import elucent.eidolon.block.HorizontalBlockBase;
import elucent.eidolon.capability.IReputation;
import elucent.eidolon.deity.Deity;
import elucent.eidolon.deity.DeityLocks;
import elucent.eidolon.particle.Particles;
import elucent.eidolon.ritual.Ritual;
import elucent.eidolon.tile.EffigyTileEntity;
import elucent.eidolon.tile.GobletTileEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class AnimalSacrificeSpell extends StaticSpell {
    Deity deity;

    public AnimalSacrificeSpell(ResourceLocation name, Deity deity, Sign... signs) {
        super(name, signs);
        this.deity = deity;
    }

    @Override
    public boolean canCast(Level world, BlockPos pos, Player player) {
        if (!world.getCapability(IReputation.INSTANCE).isPresent()) return false;
        if (!world.getCapability(IReputation.INSTANCE).resolve().get().canPray(player, getRegistryName(), world.getGameTime())) return false;
        if (world.getCapability(IReputation.INSTANCE).resolve().get().getReputation(player.getUUID(), deity.getId()) < 3.0) return false;
        List<GobletTileEntity> goblets = Ritual.getTilesWithinAABB(GobletTileEntity.class, world, new AABB(pos.offset(-4, -4, -4), pos.offset(5, 5, 5)));
        List<EffigyTileEntity> effigies = Ritual.getTilesWithinAABB(EffigyTileEntity.class, world, new AABB(pos.offset(-4, -4, -4), pos.offset(5, 5, 5)));
        if (effigies.size() == 0 || goblets.size() == 0) return false;
        EffigyTileEntity effigy = effigies.stream().min(Comparator.comparingDouble((e) -> e.getBlockPos().distSqr(pos))).get();
        GobletTileEntity goblet = goblets.stream().min(Comparator.comparingDouble((e) -> e.getBlockPos().distSqr(pos))).get();
        if (goblet.getEntityType() == null) return false;
        Entity test = goblet.getEntityType().create(world);
        return test instanceof Animal && effigy.ready();
    }

    @Override
    public void cast(Level world, BlockPos pos, Player player) {
        List<GobletTileEntity> goblets = Ritual.getTilesWithinAABB(GobletTileEntity.class, world, new AABB(pos.offset(-4, -4, -4), pos.offset(5, 5, 5)));
        List<EffigyTileEntity> effigies = Ritual.getTilesWithinAABB(EffigyTileEntity.class, world, new AABB(pos.offset(-4, -4, -4), pos.offset(5, 5, 5)));
        if (effigies.size() == 0 || goblets.size() == 0) return;
        EffigyTileEntity effigy = effigies.stream().min(Comparator.comparingDouble((e) -> e.getBlockPos().distSqr(pos))).get();
        GobletTileEntity goblet = goblets.stream().min(Comparator.comparingDouble((e) -> e.getBlockPos().distSqr(pos))).get();
        if (!world.isClientSide) {
            effigy.pray();
            goblet.setEntityType(null);
            AltarInfo info = AltarInfo.getAltarInfo(world, effigy.getBlockPos());
            world.getCapability(IReputation.INSTANCE, null).ifPresent((rep) -> {
                rep.pray(player, getRegistryName(), world.getGameTime());
                double prev = rep.getReputation(player, deity.getId());
                rep.unlock(player, deity.getId(), DeityLocks.SACRIFICE_MOB);
                rep.addReputation(player, deity.getId(), 3.0 + 0.5 * info.getPower());
            });
        }
        else {
            world.playSound(player, effigy.getBlockPos(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.NEUTRAL, 10000.0F, 0.6F + world.random.nextFloat() * 0.2F);
            world.playSound(player, effigy.getBlockPos(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.NEUTRAL, 2.0F, 0.5F + world.random.nextFloat() * 0.2F);
            BlockState state = world.getBlockState(effigy.getBlockPos());
            Direction dir = state.getValue(HorizontalBlockBase.HORIZONTAL_FACING);
            Direction tangent = dir.getClockWise();
            float x = effigy.getBlockPos().getX() + 0.5f + dir.getStepX() * 0.21875f;
            float y = effigy.getBlockPos().getY() + 0.8125f;
            float z = effigy.getBlockPos().getZ() + 0.5f + dir.getStepZ() * 0.21875f;
            Particles.create(Registry.FLAME_PARTICLE)
                .setColor(Signs.BLOOD_SIGN.getRed(), Signs.BLOOD_SIGN.getGreen(), Signs.BLOOD_SIGN.getBlue())
                .setAlpha(0.5f, 0)
                .setScale(0.125f, 0.0625f)
                .randomOffset(0.01f)
                .randomVelocity(0.0025f).addVelocity(0, 0.005f, 0)
                .repeat(world, x + 0.09375f * tangent.getStepX(), y, z + 0.09375f * tangent.getStepZ(), 8);
            Particles.create(Registry.FLAME_PARTICLE)
                .setColor(Signs.BLOOD_SIGN.getRed(), Signs.BLOOD_SIGN.getGreen(), Signs.BLOOD_SIGN.getBlue())
                .setAlpha(0.5f, 0)
                .setScale(0.1875f, 0.125f)
                .randomOffset(0.01f)
                .randomVelocity(0.0025f).addVelocity(0, 0.005f, 0)
                .repeat(world, x - 0.09375f * tangent.getStepX(), y, z - 0.09375f * tangent.getStepZ(), 8);
        }
    }
}
