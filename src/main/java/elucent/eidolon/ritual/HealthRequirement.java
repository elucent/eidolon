package elucent.eidolon.ritual;

import elucent.eidolon.Registry;
import elucent.eidolon.network.Networking;
import elucent.eidolon.network.RitualConsumePacket;
import net.minecraft.block.Block;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class HealthRequirement implements IRequirement {
    float health;

    public HealthRequirement(float health) {
        this.health = health;
    }

    @Override
    public RequirementInfo isMet(Ritual ritual, World world, BlockPos pos) {
        List<CreatureEntity> entities = world.getEntitiesWithinAABB(CreatureEntity.class, Ritual.getDefaultBounds(pos), (entity) -> !entity.isEntityUndead());
        List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, Ritual.getDefaultBounds(pos));
        List<LivingEntity> targets = new ArrayList<>();
        targets.addAll(entities);
        targets.addAll(players);
        float acc = 0;
        for (int i = 0; i < targets.size(); i ++) {
            acc += targets.get(i).getHealth();
            if (acc >= this.health) return RequirementInfo.TRUE;
        }
        return RequirementInfo.FALSE;
    }

    public void whenMet(Ritual ritual, World world, BlockPos pos, RequirementInfo info) {
        List<CreatureEntity> entities = world.getEntitiesWithinAABB(CreatureEntity.class, Ritual.getDefaultBounds(pos), (entity) -> !entity.isEntityUndead());
        List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, Ritual.getDefaultBounds(pos));
        List<LivingEntity> targets = new ArrayList<>();
        targets.addAll(entities);
        targets.addAll(players);
        float acc = 0;
        for (int i = 0; i < targets.size(); i ++) {
            float targetHealth = targets.get(i).getHealth();
            if (this.health - acc < targetHealth)
                targets.get(i).attackEntityFrom(Registry.RITUAL_DAMAGE, this.health - acc);
            else targets.get(i).attackEntityFrom(Registry.RITUAL_DAMAGE, targetHealth);

            acc += targetHealth;
            if (!world.isRemote) Networking.sendToTracking(world, pos, new RitualConsumePacket(targets.get(i).getPosition(), pos, ritual.getRed(), ritual.getGreen(), ritual.getBlue()));
            if (acc >= this.health) return;
        }
    }
}
