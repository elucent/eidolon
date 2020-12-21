package elucent.eidolon.item;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.item.model.WarlockArmorModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WarlockRobesItem extends ArmorItem {
    private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};

    public static class Material implements IArmorMaterial {
        @Override
        public int getDurability(EquipmentSlotType slot) {
            return MAX_DAMAGE_ARRAY[slot.getIndex()] * 21;
        }

        @Override
        public int getDamageReductionAmount(EquipmentSlotType slot) {
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
        public int getEnchantability() {
            return 25;
        }

        @Override
        public SoundEvent getSoundEvent() {
            return ArmorMaterial.LEATHER.getSoundEvent();
        }

        @Override
        public Ingredient getRepairMaterial() {
            return Ingredient.fromStacks(new ItemStack(Registry.WICKED_WEAVE.get()));
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

    public WarlockRobesItem(EquipmentSlotType slot, Properties builderIn) {
        super(Material.INSTANCE, slot, builderIn);
    }

    WarlockArmorModel model = null;

    @OnlyIn(Dist.CLIENT)
    @Override
    public WarlockArmorModel getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlotType slot, BipedModel defaultModel) {
        if (model == null) model = new WarlockArmorModel(slot);
        float pticks = Minecraft.getInstance().getRenderPartialTicks();
        float f = MathHelper.interpolateAngle(pticks, entity.prevRenderYawOffset, entity.renderYawOffset);
        float f1 = MathHelper.interpolateAngle(pticks, entity.prevRotationYawHead, entity.rotationYawHead);
        float netHeadYaw = f1 - f;
        float netHeadPitch = MathHelper.lerp(pticks, entity.prevRotationPitch, entity.rotationPitch);
        model.setRotationAngles(entity, entity.limbSwing, entity.limbSwingAmount, entity.ticksExisted + pticks, netHeadYaw, netHeadPitch);
        return model;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return Eidolon.MODID + ":textures/entity/warlock_robes.png";
    }
}
