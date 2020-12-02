package elucent.eidolon.item;

import elucent.eidolon.Registry;
import elucent.eidolon.capability.KnowledgeProvider;
import elucent.eidolon.capability.ReputationProvider;
import elucent.eidolon.entity.SoulfireProjectileEntity;
import elucent.eidolon.network.KnowledgeUpdatePacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.spell.Sign;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class SoulfireWandItem extends Item {
    public SoulfireWandItem(Properties builderIn) {
        super(builderIn);
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

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity entity, Hand hand) {
        ItemStack stack = entity.getHeldItem(hand);
        if (!entity.isSwingInProgress) {
            if (!world.isRemote) {
                Vector3d pos = entity.getPositionVec().add(entity.getLookVec().scale(0.5)).add(0.5 * Math.sin(Math.toRadians(225 - entity.rotationYawHead)), entity.getHeight() * 2 / 3, 0.5 * Math.cos(Math.toRadians(225 - entity.rotationYawHead)));
                Vector3d vel = entity.getEyePosition(0).add(entity.getLookVec().scale(40)).subtract(pos).scale(1.0 / 20);
                world.addEntity(new SoulfireProjectileEntity(Registry.SOULFIRE_PROJECTILE.get(), world).shoot(
                    pos.x, pos.y, pos.z, vel.x, vel.y, vel.z, entity.getUniqueID()
                ));
                world.playSound(null, pos.x, pos.y, pos.z, Registry.CAST_SOULFIRE_EVENT.get(), SoundCategory.NEUTRAL, 0.75f, random.nextFloat() * 0.2f + 0.9f);
                stack.damageItem(1, entity, (player) -> {
                    player.sendBreakAnimation(hand);
                });
            }
            entity.swingArm(hand);
            return ActionResult.resultSuccess(stack);
        }
        return ActionResult.resultPass(stack);
    }
}
