package elucent.eidolon.item;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.item.model.WarlockArmorModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WarlockRobesItem extends ArmorItem {
    private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};

    public static class Material implements IArmorMaterial {
        @Override
        public int getDurabilityForSlot(EquipmentSlot slot) {
            return MAX_DAMAGE_ARRAY[slot.getIndex()] * 21;
        }

        @Override
        public int getDefenseForSlot(EquipmentSlot slot) {
            switch (slot) {
                case CHEST:
                    return 7;
                case HEAD:
                    return 3;
                case FEET:
                    return 2;
                default:
                    return 0;
            }
        }

        @Override
        public int getEnchantmentValue() {
            return 25;
        }

        @Override
        public SoundEvent getEquipSound() {
            return ArmorMaterial.LEATHER.getEquipSound();
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(new ItemStack(Registry.WICKED_WEAVE.get()));
        }

        @Override
        public String getName() {
            return Eidolon.MODID + ":warlock_robes";
        }

        @Override
        public float getToughness() {
            return 0;
        }

        @Override
        public float getKnockbackResistance() {
            return 0;
        }

        public static final Material INSTANCE = new Material();
    }

    public WarlockRobesItem(EquipmentSlot slot, Properties builderIn) {
        super(Material.INSTANCE, slot, builderIn);
    }

    WarlockArmorModel model = null;

    @OnlyIn(Dist.CLIENT)
    @Override
    public WarlockArmorModel getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
        if (model == null) model = new WarlockArmorModel(slot);
        float pticks = Minecraft.getInstance().getFrameTime();
        float f = Mth.rotLerp(pticks, entity.yBodyRotO, entity.yBodyRot);
        float f1 = Mth.rotLerp(pticks, entity.yHeadRotO, entity.yHeadRot);
        float netHeadYaw = f1 - f;
        float netHeadPitch = Mth.lerp(pticks, entity.xRotO, entity.xRot);
        model.setupAnim(entity, entity.animationPosition, entity.animationSpeed, entity.tickCount + pticks, netHeadYaw, netHeadPitch);
        return model;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return Eidolon.MODID + ":textures/entity/warlock_robes.png";
    }
}
