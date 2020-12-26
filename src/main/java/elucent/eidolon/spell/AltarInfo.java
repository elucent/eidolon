package elucent.eidolon.spell;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import elucent.eidolon.block.TableBlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AltarInfo {
    static class AltarAttributes {
        public double capacity = 0, power = 0;

        public AltarAttributes() {}
    }

    Block icon = null, altar = null;

    Map<ResourceLocation, AltarAttributes> attributes = new HashMap<>();

    public static Set<BlockPos> getAltarPositions(World world, BlockPos pos) {
        Set<BlockPos> result = new HashSet<>();
        Queue<BlockPos> visit = new ArrayDeque();
        BlockState below = world.getBlockState(pos.down());
        Block b = below.getBlock();
        if (below.getBlock() instanceof TableBlockBase) visit.add(pos.down());
        while (!visit.isEmpty()) {
            BlockPos visited = visit.remove();
            if (result.contains(visited)) continue;
            result.add(visited);
            for (Direction d : BlockStateProperties.HORIZONTAL_FACING.getAllowedValues())
                if (world.getBlockState(visited.offset(d)).getBlock() == b
                    && !result.contains(visited.offset(d)))
                    visit.add(visited.offset(d));
        }
        return result;
    }

    public static AltarInfo getAltarInfo(World world, BlockPos pos) {
        AltarInfo info = new AltarInfo();
        info.icon = world.getBlockState(pos).getBlock();
        Set<BlockPos> altarPositions = getAltarPositions(world, pos);
        for (BlockPos p : altarPositions) {
            if (info.altar == null) info.altar = world.getBlockState(p).getBlock();
            BlockState state = world.getBlockState(p.up());
            AltarEntry entry = AltarEntries.find(state);
            if (entry != null) {
                entry.apply(info);
            }
        }
        return info;
    }

    protected void increaseCapacity(ResourceLocation key, double value) {
        AltarAttributes attrs = attributes.computeIfAbsent(key, (k) -> new AltarAttributes());
        attrs.capacity = Math.max(attrs.capacity, value);
    }

    protected void increasePower(ResourceLocation key, double value) {
        AltarAttributes attrs = attributes.computeIfAbsent(key, (k) -> new AltarAttributes());
        attrs.power = Math.max(attrs.power, value);
    }

    public double getCapacity() {
        double sum = 0;
        for (Map.Entry<ResourceLocation, AltarAttributes> key : attributes.entrySet())
            sum += key.getValue().capacity;
        return sum;
    }

    public double getPower() {
        double sum = 0;
        for (Map.Entry<ResourceLocation, AltarAttributes> key : attributes.entrySet())
            sum += key.getValue().power;
        return sum;
    }

    public Block getIcon() {
        return icon;
    }

    public Block getAltar() {
        return altar;
    }

    public boolean hasKey(ResourceLocation loc) {
        return attributes.containsKey(loc);
    }
}
