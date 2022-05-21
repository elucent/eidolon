package elucent.eidolon.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mojang.math.Vector3f;

import elucent.eidolon.Registry;
import elucent.eidolon.network.Networking;
import elucent.eidolon.network.SpellCastPacket;
import elucent.eidolon.particle.RuneParticleData;
import elucent.eidolon.particle.SignParticleData;
import elucent.eidolon.spell.Rune;
import elucent.eidolon.spell.Rune.RuneResult;
import elucent.eidolon.spell.Runes;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.SignSequence;
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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class ChantCasterEntity extends Entity implements IEntityAdditionalSpawnData {
    public static final EntityDataAccessor<CompoundTag> RUNES = SynchedEntityData.<CompoundTag>defineId(ChantCasterEntity.class, EntityDataSerializers.COMPOUND_TAG);
    public static final EntityDataAccessor<CompoundTag> SIGNS = SynchedEntityData.<CompoundTag>defineId(ChantCasterEntity.class, EntityDataSerializers.COMPOUND_TAG);
    public static final EntityDataAccessor<Integer> INDEX = SynchedEntityData.<Integer>defineId(ChantCasterEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Optional<UUID>> CASTER_ID = SynchedEntityData.<Optional<UUID>>defineId(ChantCasterEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    public static final EntityDataAccessor<Boolean> SUCCEEDED = SynchedEntityData.<Boolean>defineId(ChantCasterEntity.class, EntityDataSerializers.BOOLEAN);
    int timer = 0, deathTimer = 0;
    Vec3 look;

    public ChantCasterEntity(Level world, Player caster, List<Rune> runes, Vec3 look) {
        super(Registry.CHANT_CASTER.get(), world);
        this.look = look;
        setRunesTag(runes);
        getEntityData().set(CASTER_ID, Optional.of(caster.getUUID()));
    }

    public ChantCasterEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }
    
    protected CompoundTag getNoRunesTag() {
    	CompoundTag emptyRunes = new CompoundTag();
    	emptyRunes.put("runes", new ListTag());
    	return emptyRunes;
    }
    
    protected List<Rune> loadRunesTag() {
    	List<Rune> runes = new ArrayList<>();
    	ListTag runesTag = getEntityData().get(RUNES).getList("runes", Tag.TAG_STRING);
    	for (int i = 0; i < runesTag.size(); i ++) {
    		Rune r = Runes.find(new ResourceLocation(runesTag.getString(i)));
    		if (r != null) runes.add(r);
    	}
    	return runes;
    }
    
    protected void setRunesTag(List<Rune> runes) {
    	ListTag runesList = new ListTag();
    	CompoundTag runesTag = new CompoundTag();
    	for (Rune r : runes) runesList.add(StringTag.valueOf(r.getRegistryName().toString()));
    	runesTag.put("runes", runesList);
    	getEntityData().set(RUNES, runesTag);
    }

    @Override
    protected void defineSynchedData() {
    	getEntityData().define(RUNES, getNoRunesTag());
        getEntityData().define(SIGNS, new SignSequence().serializeNbt());
        getEntityData().define(INDEX, 0);
        getEntityData().define(CASTER_ID, Optional.empty());
        getEntityData().define(SUCCEEDED, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (deathTimer > 0) {
        	deathTimer --;
        	if (deathTimer <= 0) remove(RemovalReason.KILLED);
        	return;
        }
        if (timer > 0) {
            timer --;
            if (timer <= 0) {
                CompoundTag signData = getEntityData().get(SIGNS);
                Optional<UUID> optuuid = getEntityData().get(CASTER_ID);
                if (!level.isClientSide && optuuid.isPresent()) {
                	SignSequence seq = SignSequence.deserializeNbt(signData);
                    Spell spell = Spells.find(seq);
                    Player player = level.getPlayerByUUID(optuuid.get());
                    if (spell != null && player != null && spell.canCast(level, blockPosition(), player, seq)) {
                        spell.cast(level, blockPosition(), player, seq);
                        Networking.sendToTracking(level, blockPosition(), new SpellCastPacket(player, blockPosition(), spell, seq));
                    	getEntityData().set(SUCCEEDED, true);
                    }
                    else {
                    	level.playSound(null, blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 1.0f, 1.0f);
                    	getEntityData().set(SUCCEEDED, false);
                    }
                }
                deathTimer = 20;
                return;
            }
        }

        if (tickCount % 5 == 0) {
        	List<Rune> runes = loadRunesTag();
        	SignSequence seq = SignSequence.deserializeNbt(getEntityData().get(SIGNS));
        	Vector3f initColor = seq.getAverageColor();
        	
            int index = getEntityData().get(INDEX);
            if (index >= runes.size()) return;
            Rune rune = runes.get(index);
            RuneResult result = rune.doEffect(seq);
            if (result == RuneResult.FAIL) {
            	if (!level.isClientSide) {
            		level.playSound(null, blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 1.0f, 1.0f);
                    getEntityData().set(INDEX, runes.size());
                    getEntityData().set(SUCCEEDED, false);
            	}
                deathTimer = 20;
                return;
            }
            else {
            	Vector3f afterColor = seq.getAverageColor();
                double x = getX() + 0.1 * random.nextGaussian(),
                        y = getY() + 0.1 * random.nextGaussian(),
                        z = getZ() + 0.1 * random.nextGaussian();
                for (int i = 0; i < 2; i ++) {
                    level.addParticle(new RuneParticleData(
                		rune, 
                		initColor.x(), initColor.y(), initColor.z(), 
                		afterColor.x(), afterColor.y(), afterColor.z()
                	), x, y, z, look.x * 0.03, look.y * 0.03, look.z * 0.03);
                }
                level.playSound(null, blockPosition(), Registry.CHANT_WORD.get(), SoundSource.NEUTRAL, 0.7f, random.nextFloat() * 0.375f + 0.625f);
                if (index + 1 >= runes.size()) {
                    timer = 20;
                }
                if (!level.isClientSide) {
                	getEntityData().set(INDEX, index + 1);
                	getEntityData().set(SIGNS, seq.serializeNbt());
                }
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        getEntityData().set(RUNES, compound.getCompound("runes_tag"));
        getEntityData().set(SIGNS, compound.getCompound("signs_tag"));
        getEntityData().set(INDEX, compound.getInt("index"));
        getEntityData().set(CASTER_ID, Optional.of(compound.getUUID("caster_id")));
        look = new Vec3(compound.getDouble("lookX"), compound.getDouble("lookY"), compound.getDouble("lookZ"));
        timer = compound.getInt("timer");
        deathTimer = compound.getInt("deathTimer");
        getEntityData().set(SUCCEEDED, compound.getBoolean("succeeded"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.put("runes_tag", getEntityData().get(RUNES));
        compound.put("signs_tag", getEntityData().get(SIGNS));
        compound.putInt("index", getEntityData().get(INDEX));
        compound.putInt("timer", timer);
        compound.putUUID("caster_id", getEntityData().get(CASTER_ID).get());
        compound.putDouble("lookX", look.x);
        compound.putDouble("lookY", look.y);
        compound.putDouble("lookZ", look.z);
        compound.putInt("deathTimer", deathTimer);
        compound.putBoolean("succeeded", getEntityData().get(SUCCEEDED));
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeDouble(look.x);
		buffer.writeDouble(look.y);
		buffer.writeDouble(look.z);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf buf) {
		look = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}
}
