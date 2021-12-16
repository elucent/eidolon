package elucent.eidolon.spell;

import java.util.HashMap;
import java.util.Map;

import elucent.eidolon.Registry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class AltarEntries {
    static Map<BlockState, AltarEntry> entries = new HashMap<>();

    public static AltarEntry find(BlockState state) {
        return entries.getOrDefault(state, null);
    }

    public static void init() {
        entries.put(Blocks.TORCH.defaultBlockState(), new AltarEntry(AltarKeys.LIGHT_KEY).setPower(1));
        entries.put(Blocks.LANTERN.defaultBlockState(), new AltarEntry(AltarKeys.LIGHT_KEY).setPower(1).setCapacity(1));
        entries.put(Registry.CANDLE.get().defaultBlockState(), new AltarEntry(AltarKeys.LIGHT_KEY).setPower(2));
        entries.put(Registry.CANDLESTICK.get().defaultBlockState(), new AltarEntry(AltarKeys.LIGHT_KEY).setPower(2));

        for (int i : BlockStateProperties.ROTATION_16.getPossibleValues()) {
            entries.put(Blocks.SKELETON_SKULL.defaultBlockState().setValue(SkullBlock.ROTATION, i), new AltarEntry(AltarKeys.SKULL_KEY).setCapacity(2));
            entries.put(Blocks.ZOMBIE_HEAD.defaultBlockState().setValue(SkullBlock.ROTATION, i), new AltarEntry(AltarKeys.SKULL_KEY).setCapacity(1).setPower(1));
            entries.put(Blocks.WITHER_SKELETON_SKULL.defaultBlockState().setValue(SkullBlock.ROTATION, i), new AltarEntry(AltarKeys.SKULL_KEY).setCapacity(3).setPower(1));
        }

        entries.put(Blocks.POTTED_WARPED_ROOTS.defaultBlockState(), new AltarEntry(AltarKeys.PLANT_KEY).setPower(1));
        entries.put(Blocks.POTTED_CRIMSON_ROOTS.defaultBlockState(), new AltarEntry(AltarKeys.PLANT_KEY).setPower(1));
        entries.put(Blocks.POTTED_WARPED_FUNGUS.defaultBlockState(), new AltarEntry(AltarKeys.PLANT_KEY).setPower(2));
        entries.put(Blocks.POTTED_CRIMSON_FUNGUS.defaultBlockState(), new AltarEntry(AltarKeys.PLANT_KEY).setPower(2));
        entries.put(Blocks.POTTED_WITHER_ROSE.defaultBlockState(), new AltarEntry(AltarKeys.PLANT_KEY).setPower(3));
        entries.put(Registry.GOBLET.get().defaultBlockState(), new AltarEntry(AltarKeys.GOBLET_KEY).setCapacity(2));
    }
}
