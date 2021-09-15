package elucent.eidolon.entity;

import elucent.eidolon.Registry;
import elucent.eidolon.network.Networking;
import elucent.eidolon.network.SpellCastPacket;
import elucent.eidolon.particle.SignParticleData;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import elucent.eidolon.spell.Spell;
import elucent.eidolon.spell.Spells;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ChantCasterEntity extends Entity {
    public static final DataParameter<CompoundNBT> SIGNS = EntityDataManager.<CompoundNBT>createKey(ChantCasterEntity.class, DataSerializers.COMPOUND_NBT);
    public static final DataParameter<Integer> INDEX = EntityDataManager.<Integer>createKey(ChantCasterEntity.class, DataSerializers.VARINT);
    public static final DataParameter<Optional<UUID>> CASTER_ID = EntityDataManager.<Optional<UUID>>createKey(ChantCasterEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    int timer = 0;

    public ChantCasterEntity(World world, PlayerEntity caster, List<Sign> signs) {
        super(Registry.CHANT_CASTER.get(), world);
        ListNBT list = new ListNBT();
        for (Sign sign : signs) list.add(StringNBT.valueOf(sign.getRegistryName().toString()));
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("signs", list);
        getDataManager().set(SIGNS, nbt);
        getDataManager().set(CASTER_ID, Optional.of(caster.getUniqueID()));
    }

    public ChantCasterEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    protected void registerData() {
        getDataManager().register(SIGNS, new CompoundNBT());
        getDataManager().register(INDEX, 0);
        getDataManager().register(CASTER_ID, Optional.empty());
    }

    @Override
    public void tick() {
        super.tick();

        if (timer > 0) {
            timer --;
            if (timer == 0) {
                ListNBT signData = getDataManager().get(SIGNS).getList("signs", Constants.NBT.TAG_STRING);
                Optional<UUID> optuuid = getDataManager().get(CASTER_ID);
                if (!world.isRemote && optuuid.isPresent()) {
                    List<Sign> signs = new ArrayList<>();
                    for (int i = 0; i < signData.size(); i++)
                        signs.add(Signs.find(new ResourceLocation(signData.getString(i))));
                    Spell spell = Spells.find(signs);
                    PlayerEntity player = world.getPlayerByUuid(optuuid.get());
                    if (spell != null && player != null && spell.canCast(world, getPosition(), player, signs)) {
                        spell.cast(world, getPosition(), player, signs);
                        Networking.sendToTracking(world, getPosition(), new SpellCastPacket(player, getPosition(), spell, signs));
                    }
                    else world.playSound(null, getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 1.0f, 1.0f);
                }
                if (!world.isRemote) setDead();
                return;
            }
        }

        if (ticksExisted % 5 == 0) {
            ListNBT signs = getDataManager().get(SIGNS).getList("signs", Constants.NBT.TAG_STRING);
            int index = getDataManager().get(INDEX);
            if (index >= signs.size()) return;
            Sign sign = Signs.find(new ResourceLocation(signs.getString(index)));
            double x = getPosX() + 0.2 * rand.nextGaussian(),
                y = getPosY() + 0.2 * rand.nextGaussian(),
                z = getPosZ() + 0.2 * rand.nextGaussian();
            for (int i = 0; i < 2; i ++) {
                world.addParticle(new SignParticleData(sign), x, y, z, 0, 0, 0);
            }
            world.playSound(null, getPosition(), Registry.CHANT_WORD.get(), SoundCategory.NEUTRAL, 0.7f, rand.nextFloat() * 0.375f + 0.625f);
            if (index + 1 >= signs.size()) {
                timer = 40;
            }
            if (!world.isRemote) getDataManager().set(INDEX, index + 1);
        }
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        getDataManager().set(SIGNS, compound.getCompound("signs_tag"));
        getDataManager().set(INDEX, compound.getInt("index"));
        getDataManager().set(CASTER_ID, Optional.of(compound.getUniqueId("caster_id")));
        timer = compound.getInt("timer");
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.put("signs_tag", getDataManager().get(SIGNS));
        compound.putInt("index", getDataManager().get(INDEX));
        compound.putInt("timer", timer);
        compound.putUniqueId("caster_id", getDataManager().get(CASTER_ID).get());
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
