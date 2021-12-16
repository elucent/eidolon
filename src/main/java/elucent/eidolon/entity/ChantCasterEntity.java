package elucent.eidolon.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import elucent.eidolon.Registry;
import elucent.eidolon.network.Networking;
import elucent.eidolon.network.SpellCastPacket;
import elucent.eidolon.particle.SignParticleData;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import elucent.eidolon.spell.Spell;
import elucent.eidolon.spell.Spells;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.network.NetworkHooks;

public class ChantCasterEntity extends Entity {
    public static final EntityDataAccessor<CompoundTag> SIGNS = SynchedEntityData.<CompoundTag>defineId(ChantCasterEntity.class, EntityDataSerializers.COMPOUND_TAG);
    public static final EntityDataAccessor<Integer> INDEX = SynchedEntityData.<Integer>defineId(ChantCasterEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Optional<UUID>> CASTER_ID = SynchedEntityData.<Optional<UUID>>defineId(ChantCasterEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    int timer = 0;

    public ChantCasterEntity(Level world, Player caster, List<Sign> signs) {
        super(Registry.CHANT_CASTER.get(), world);
        ListTag list = new ListTag();
        for (Sign sign : signs) list.add(StringTag.valueOf(sign.getRegistryName().toString()));
        CompoundTag nbt = new CompoundTag();
        nbt.put("signs", list);
        getEntityData().set(SIGNS, nbt);
        getEntityData().set(CASTER_ID, Optional.of(caster.getUUID()));
    }

    public ChantCasterEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    protected void defineSynchedData() {
        getEntityData().define(SIGNS, new CompoundTag());
        getEntityData().define(INDEX, 0);
        getEntityData().define(CASTER_ID, Optional.empty());
    }

    @Override
    public void tick() {
        super.tick();

        if (timer > 0) {
            timer --;
            if (timer == 0) {
                ListTag signData = getEntityData().get(SIGNS).getList("signs", Tag.TAG_STRING);
                Optional<UUID> optuuid = getEntityData().get(CASTER_ID);
                if (!level.isClientSide && optuuid.isPresent()) {
                    List<Sign> signs = new ArrayList<>();
                    for (int i = 0; i < signData.size(); i++)
                        signs.add(Signs.find(new ResourceLocation(signData.getString(i))));
                    Spell spell = Spells.find(signs);
                    Player player = level.getPlayerByUUID(optuuid.get());
                    if (spell != null && player != null && spell.canCast(level, blockPosition(), player, signs)) {
                        spell.cast(level, blockPosition(), player, signs);
                        Networking.sendToTracking(level, blockPosition(), new SpellCastPacket(player, blockPosition(), spell, signs));
                    }
                    else level.playSound(null, blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 1.0f, 1.0f);
                }
                if (!level.isClientSide) removeAfterChangingDimensions();
                return;
            }
        }

        if (tickCount % 5 == 0) {
            ListTag signs = getEntityData().get(SIGNS).getList("signs", Tag.TAG_STRING);
            int index = getEntityData().get(INDEX);
            if (index >= signs.size()) return;
            Sign sign = Signs.find(new ResourceLocation(signs.getString(index)));
            double x = getX() + 0.2 * random.nextGaussian(),
                y = getY() + 0.2 * random.nextGaussian(),
                z = getZ() + 0.2 * random.nextGaussian();
            for (int i = 0; i < 2; i ++) {
                level.addParticle(new SignParticleData(sign), x, y, z, 0, 0, 0);
            }
            level.playSound(null, blockPosition(), Registry.CHANT_WORD.get(), SoundSource.NEUTRAL, 0.7f, random.nextFloat() * 0.375f + 0.625f);
            if (index + 1 >= signs.size()) {
                timer = 40;
            }
            if (!level.isClientSide) getEntityData().set(INDEX, index + 1);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        getEntityData().set(SIGNS, compound.getCompound("signs_tag"));
        getEntityData().set(INDEX, compound.getInt("index"));
        getEntityData().set(CASTER_ID, Optional.of(compound.getUUID("caster_id")));
        timer = compound.getInt("timer");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.put("signs_tag", getEntityData().get(SIGNS));
        compound.putInt("index", getEntityData().get(INDEX));
        compound.putInt("timer", timer);
        compound.putUUID("caster_id", getEntityData().get(CASTER_ID).get());
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
