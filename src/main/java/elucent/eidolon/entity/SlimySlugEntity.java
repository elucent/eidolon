package elucent.eidolon.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;
import java.util.UUID;

import elucent.eidolon.capability.IReputation;
import elucent.eidolon.deity.Deities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.extensions.IForgeEntity;
import net.minecraftforge.registries.ForgeRegistries;

public class SlimySlugEntity extends PathfinderMob implements IForgeEntity {
    private static final Ingredient TEMPTATION_ITEMS = Ingredient.of(Items.PUMPKIN_SEEDS);
    float yRotTrail = 0.0f;
    float squishAmount = 1.0f;
    public static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.<Integer>defineId(SlimySlugEntity.class, EntityDataSerializers.INT);
    
    public SlimySlugEntity(EntityType<SlimySlugEntity> type, Level worldIn) {
        super(type, worldIn);
        registerGoals();
        yRotTrail = this.getYRot();
        getEntityData().set(TYPE, 0);
    }

    @Override
	public void onAddedToWorld() {
    	Biome b = level.getBiome(getOnPos());
    	if (Biomes.LUSH_CAVES.getRegistryName().equals(b.getRegistryName())) {
            getEntityData().set(TYPE, 0);
    	}
    	else if (Biomes.OLD_GROWTH_PINE_TAIGA.location().equals(b.getRegistryName())
    			 || Biomes.OLD_GROWTH_SPRUCE_TAIGA.location().equals(b.getRegistryName())) {
            getEntityData().set(TYPE, 1);
    	}
    	else if (Biomes.FLOWER_FOREST.location().equals(b.getRegistryName())) {
            getEntityData().set(TYPE, 2);
    	}
    }

    @Override
    protected void defineSynchedData() {
    	super.defineSynchedData();
        getEntityData().define(TYPE, 0);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, 
        		(e) -> ((Player)e).getGameProfile().getId().equals(UUID.fromString("0ca54301-6170-4c44-b3e0-b8afa6b81ed2"))));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.4D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, TEMPTATION_ITEMS, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 8.0D)
            .add(Attributes.MOVEMENT_SPEED, (double)0.1F)
            .add(Attributes.ARMOR, 0.0D)
            .add(Attributes.ATTACK_DAMAGE, 999.0D)
            .build();
    }
    
    public void tick() {
    	super.tick();
    	yRotTrail = Mth.rotLerp(yRotTrail, getYRot(), 0.2f);
    }
    
    @Override
    public boolean hurt(DamageSource source, float amt) {
    	if (source != null && source.getEntity() instanceof Player p 
    		&& p.getGameProfile().getId().equals(UUID.fromString("0ca54301-6170-4c44-b3e0-b8afa6b81ed2"))) {
    		// SammySemicolon is not allowed to hurt slugs >:/
    		p.hurt(source, amt);
    		return false;
    	}
    	return super.hurt(source, amt);
    }
}
