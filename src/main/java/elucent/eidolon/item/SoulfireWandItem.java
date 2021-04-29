package elucent.eidolon.item;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import elucent.eidolon.capability.KnowledgeProvider;
import elucent.eidolon.capability.ReputationProvider;
import elucent.eidolon.deity.Deities;
import elucent.eidolon.deity.DeityLocks;
import elucent.eidolon.entity.SoulfireProjectileEntity;
import elucent.eidolon.network.KnowledgeUpdatePacket;
import elucent.eidolon.network.Networking;
import elucent.eidolon.spell.Sign;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class SoulfireWandItem extends WandItem {
    public SoulfireWandItem(Properties builderIn) {
        super(builderIn);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity entity, Hand hand) {
        ItemStack stack = entity.getHeldItem(hand);
        if (!entity.isSwingInProgress) {
            if (!world.isRemote) {
                if (hand == Hand.MAIN_HAND){
                    Vector3d pos = entity.getPositionVec().add(entity.getLookVec().scale(0.5)).add(0.5 * Math.sin(Math.toRadians(225 - entity.rotationYawHead)), entity.getHeight() * 2 / 3, 0.5 * Math.cos(Math.toRadians(225 - entity.rotationYawHead)));
                }else{
                    Vector3d pos = entity.getPositionVec().add(entity.getLookVec().scale(0.5)).add(-0.5 * Math.sin(Math.toRadians(225 - entity.rotationYawHead)), entity.getHeight() * 2 / 3, -0.5 * Math.cos(Math.toRadians(225 - entity.rotationYawHead)));
                }
                Vector3d vel = entity.getEyePosition(0).add(entity.getLookVec().scale(40)).subtract(pos).scale(1.0 / 20);
                world.addEntity(new SoulfireProjectileEntity(Registry.SOULFIRE_PROJECTILE.get(), world).shoot(
                    pos.x, pos.y, pos.z, vel.x, vel.y, vel.z, entity.getUniqueID()
                ));
                world.playSound(null, pos.x, pos.y, pos.z, Registry.CAST_SOULFIRE_EVENT.get(), SoundCategory.NEUTRAL, 0.75f, random.nextFloat() * 0.2f + 0.9f);
                stack.damageItem(1, entity, (player) -> {
                    player.sendBreakAnimation(hand);
                });

                try {
                    Template t = ((ServerWorld)world).getStructureTemplateManager().getTemplate(new ResourceLocation("eidolon", "corridor"));
                    BlockPos d = t.getSize();
                    Rotation r = Rotation.values()[entity.getHorizontalFacing().getHorizontalIndex()];
                    BlockPos o = new BlockPos(-d.getX() / 2, -d.getY() / 2, -d.getZ() / 2);
                    BlockPos s = new BlockPos(Math.max(o.getX(), o.getZ()), o.getY(), Math.max(o.getX(), o.getZ()));
                    t.func_237152_b_((ServerWorld)world, entity.getPosition().down(8).add(o.rotate(r)).subtract(s), new PlacementSettings().setRotation(r), random);
                } catch (Exception e) {
                    //
                }
            }
            entity.swingArm(hand);
            return ActionResult.resultSuccess(stack);
        }
        return ActionResult.resultPass(stack);
    }
}
