package elucent.eidolon.entity;

import java.util.Comparator;
import java.util.List;

import elucent.eidolon.Registry;
import elucent.eidolon.mixin.AbstractArrowMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class AngelArrowEntity extends AbstractArrow implements IEntityAdditionalSpawnData {
    AbstractArrow internal = null;

    public AngelArrowEntity(EntityType<? extends AbstractArrow> type, Level worldIn) {
        super(type, worldIn);
    }
    public AngelArrowEntity(Level worldIn, LivingEntity shooter) {
        super(Registry.ANGEL_ARROW.get(), shooter, worldIn);
    }

    public void setArrow(AbstractArrow entity) {
        this.internal = entity;
        internal.copyPosition(this);
    }

    public static float lerpDegrees(float a, float b, float t) {
        float d1 = Math.abs(b - a), d2 = Math.abs((b - 360) - a), d3 = Math.abs((b + 360) - a);
        if (d2 > d3 && d2 > d1) {
            b -= 360;
            d1 = d2;
        }
        if (d3 > d2 && d3 > d1) {
            b += 360;
            d1 = d3;
        }
        return a + d1 * t;
    }

    @Override
    public void tick() {
        if (internal == null) {
            removeAfterChangingDimensions();
            return;
        }
        super.tick();
        internal.tick();
        internal.xo = xo;
        internal.yo = yo;
        internal.zo = zo;
        internal.xRotO = xRotO;
        internal.yRotO = yRotO;
        internal.copyPosition(this);
        internal.setDeltaMovement(getDeltaMovement());
        if (!inGround) {
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(12), (e) -> {
                return e != getOwner() && e.isAlive() && (!level.isClientSide || e != Minecraft.getInstance().player);
            });
            if (!entities.isEmpty()) {
                for (Entity e : entities) System.out.println(e);
                LivingEntity nearest = entities.stream().min(Comparator.comparingDouble((e) -> e.distanceToSqr(this))).get();
                Vec3 diff = nearest.position().add(0, nearest.getBbHeight() / 2, 0).subtract(position());
                double speed = getDeltaMovement().length();
                Vec3 newmotion = getDeltaMovement().add(diff.normalize().scale(speed)).scale(0.5);
                if (newmotion.length() == 0) newmotion = newmotion.add(0.01, 0, 0); // avoid divide by zero
                setDeltaMovement(newmotion.scale(speed / newmotion.length()));
            }
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return internal == null ? ItemStack.EMPTY :
            ((AbstractArrowMixin)(Object)internal).callGetPickupItem();
    }

    @Override
    public void onHitEntity(EntityHitResult result) {
        ((AbstractArrowMixin)(Object)internal).callOnHitEntity(result);
        if (internal.isRemoved()) remove(RemovalReason.DISCARDED);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString("type", internal.getType().getRegistryName().toString());
        nbt.put("data", internal.serializeNBT());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        ResourceLocation rl = new ResourceLocation(nbt.getString("type"));
        EntityType<?> type = ForgeRegistries.ENTITIES.getValue(rl);
        if (type == null) removeAfterChangingDimensions();

        internal = (AbstractArrow)type.create(level);
        internal.deserializeNBT(nbt.getCompound("data"));
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        CompoundTag extra = new CompoundTag();
        extra.putString("type", internal.getType().getRegistryName().toString());
        extra.put("data", internal.serializeNBT());
        buffer.writeNbt(extra);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        CompoundTag extra = additionalData.readNbt();
        ResourceLocation rl = new ResourceLocation(extra.getString("type"));
        EntityType<?> type = ForgeRegistries.ENTITIES.getValue(rl);
        if (type == null) removeAfterChangingDimensions();

        internal = (AbstractArrow)type.create(level);
        internal.deserializeNBT(extra.getCompound("data"));
    }
}
