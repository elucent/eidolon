package elucent.eidolon.spell;

import elucent.eidolon.Registry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SkullBlock;
import net.minecraft.state.properties.BlockStateProperties;

import java.util.HashMap;
import java.util.Map;

public class AltarEntries {
    static Map<BlockState, AltarEntry> entries = new HashMap<>();

    public static AltarEntry find(BlockState state) {
        return entries.getOrDefault(state, null);
    }

    public static void init() {
        entries.put(Blocks.TORCH.getDefaultState(), new AltarEntry(AltarKeys.LIGHT_KEY).setPower(1));
        entries.put(Blocks.LANTERN.getDefaultState(), new AltarEntry(AltarKeys.LIGHT_KEY).setPower(1).setCapacity(1));
        entries.put(Registry.CANDLE.get().getDefaultState(), new AltarEntry(AltarKeys.LIGHT_KEY).setPower(2));
        entries.put(Registry.CANDLESTICK.get().getDefaultState(), new AltarEntry(AltarKeys.LIGHT_KEY).setPower(2));

        for (int i : BlockStateProperties.ROTATION_0_15.getAllowedValues()) {
            entries.put(Blocks.SKELETON_SKULL.getDefaultState().with(SkullBlock.ROTATION, i), new AltarEntry(AltarKeys.SKULL_KEY).setCapacity(2));
            entries.put(Blocks.ZOMBIE_HEAD.getDefaultState().with(SkullBlock.ROTATION, i), new AltarEntry(AltarKeys.SKULL_KEY).setCapacity(1).setPower(1));
            entries.put(Blocks.WITHER_SKELETON_SKULL.getDefaultState().with(SkullBlock.ROTATION, i), new AltarEntry(AltarKeys.SKULL_KEY).setCapacity(3).setPower(1));
        }

        entries.put(Blocks.POTTED_WARPED_ROOTS.getDefaultState(), new AltarEntry(AltarKeys.PLANT_KEY).setPower(1));
        entries.put(Blocks.POTTED_CRIMSON_ROOTS.getDefaultState(), new AltarEntry(AltarKeys.PLANT_KEY).setPower(1));
        entries.put(Blocks.POTTED_WARPED_FUNGUS.getDefaultState(), new AltarEntry(AltarKeys.PLANT_KEY).setPower(2));
        entries.put(Blocks.POTTED_CRIMSON_FUNGUS.getDefaultState(), new AltarEntry(AltarKeys.PLANT_KEY).setPower(2));
        entries.put(Blocks.POTTED_WITHER_ROSE.getDefaultState(), new AltarEntry(AltarKeys.PLANT_KEY).setPower(3));
        entries.put(Registry.GOBLET.get().getDefaultState(), new AltarEntry(AltarKeys.GOBLET_KEY).setCapacity(2));
    }
}
