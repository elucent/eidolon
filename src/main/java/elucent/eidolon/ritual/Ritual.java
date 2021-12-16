package elucent.eidolon.ritual;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import elucent.eidolon.util.ColorUtil;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;

public abstract class Ritual {
    ResourceLocation name = null;
    int color;
    ResourceLocation symbol;
    List<IRequirement> stepRequirements = new ArrayList<>(), continuousRequirements = new ArrayList<>();

    public Ritual(ResourceLocation symbol, float r, float g, float b) {
        this.symbol = symbol;
        this.color = ColorUtil.packColor(255, (int)(r * 255), (int)(g * 255), (int)(b * 255));
    }

    public Ritual(ResourceLocation symbol, int color) {
        this.symbol = symbol;
        this.color = color;
    }

    public ResourceLocation getSymbol() {
        return this.symbol;
    }

    public Ritual setRegistryName(String domain, String path) {
        this.name = new ResourceLocation(domain, path);
        return this;
    }

    public Ritual setRegistryName(ResourceLocation name) {
        this.name = name;
        return this;
    }

    public ResourceLocation getRegistryName() {
        return this.name;
    }

    public Ritual addRequirement(IRequirement requirement) {
        stepRequirements.add(requirement);
        return this;
    }

    public Ritual addInvariant(IRequirement requirement) {
        continuousRequirements.add(requirement);
        return this;
    }

    public int getColor() {
        return color;
    }

    public float getRed() {
        return ColorUtil.getRed(color) / 255.0f;
    }

    public float getGreen() {
        return ColorUtil.getGreen(color) / 255.0f;
    }

    public float getBlue() {
        return ColorUtil.getBlue(color) / 255.0f;
    }

    public List<IRequirement> getRequirements() {
        return stepRequirements;
    }

    public enum SetupResult {
        FAIL,
        PASS,
        SUCCEED
    }

    public enum RitualResult {
        PASS,
        TERMINATE
    }

    public SetupResult setup(Level world, BlockPos pos, int step) {
        if (step >= stepRequirements.size()) return SetupResult.SUCCEED;
        for (IRequirement req : continuousRequirements) {
            RequirementInfo info = req.isMet(this, world, pos);
            if (!info.isMet()) return SetupResult.FAIL;
            else req.whenMet(this, world, pos, info);
        }
        IRequirement req = stepRequirements.get(step);
        RequirementInfo info = req.isMet(this, world, pos);
        if (!info.isMet()) return SetupResult.FAIL;
        else req.whenMet(this, world, pos, info);
        return SetupResult.PASS;
    }

    public AABB getSearchBounds(BlockPos pos) {
        return getDefaultBounds(pos);
    }

    public static AABB getDefaultBounds(BlockPos pos) {
        return new AABB(pos.getX() - 8, pos.getY() - 6, pos.getZ() - 8, pos.getX() + 9, pos.getY() + 11, pos.getZ() + 9);
    }

    public RitualResult tick(Level world, BlockPos pos) {
        return RitualResult.PASS;
    }

    public RitualResult start(Level world, BlockPos pos) {
        return RitualResult.PASS;
    }

    public static <T> List<T> getTilesWithinAABB(Class<T> type, Level world, AABB bb) {
        List<T> tileList = new ArrayList<>();
        for (int i = (int)Math.floor(bb.minX); i < (int)Math.ceil(bb.maxX) + 16; i += 16) {
            for (int j = (int)Math.floor(bb.minZ); j < (int)Math.ceil(bb.maxZ) + 16; j += 16) {
                ChunkAccess c = world.getChunk(new BlockPos(i, 0, j));
                Set<BlockPos> tiles = c.getBlockEntitiesPos();
                for (BlockPos p : tiles) if (bb.contains(p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5)) {
                    BlockEntity t = world.getBlockEntity(p);
                    if (type.isInstance(t)) tileList.add((T)t);
                }
            }
        }
        return tileList;
    }
}
