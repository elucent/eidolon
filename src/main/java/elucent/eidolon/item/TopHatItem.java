package elucent.eidolon.item;

import java.util.List;

import elucent.eidolon.Eidolon;
import elucent.eidolon.item.model.TopHatModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TopHatItem extends ArmorItem {
    private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};

    public static class Material implements IArmorMaterial {
        @Override
        public int getDurability(EquipmentSlotType slot) {
            return MAX_DAMAGE_ARRAY[slot.getIndex()] * 7;
        }

        @Override
        public int getDamageReductionAmount(EquipmentSlotType slot) {
            return 1;
        }

        @Override
        public int getEnchantability() {
            return 12;
        }

        @Override
        public SoundEvent getSoundEvent() {
            return ArmorMaterial.LEATHER.getSoundEvent();
        }

        @Override
        public Ingredient getRepairMaterial() {
            return Ingredient.fromStacks(new ItemStack(Items.BLACK_WOOL));
        }

        @Override
        public String getName() {
            return Eidolon.MODID + ":top_hat";
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

    String loreTag = null;

    public Item setLore(String tag) {
        this.loreTag = tag;
        return this;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (this.loreTag != null) {
            tooltip.add(new StringTextComponent(""));
            tooltip.add(new StringTextComponent("" + TextFormatting.DARK_PURPLE + TextFormatting.ITALIC + I18n.format(this.loreTag)));
        }
    }

    public TopHatItem(Properties builderIn) {
        super(Material.INSTANCE, EquipmentSlotType.HEAD, builderIn);
    }

    TopHatModel model = null;

    @OnlyIn(Dist.CLIENT)
    @Override
    public TopHatModel getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlotType slot, BipedModel defaultModel) {
        if (model == null) model = new TopHatModel();
        return model;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return Eidolon.MODID + ":textures/entity/hat.png";
    }
}
