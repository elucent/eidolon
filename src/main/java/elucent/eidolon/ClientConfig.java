package elucent.eidolon;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    // graphics
    public static ForgeConfigSpec.ConfigValue<Boolean> BETTER_LAYERING;
    // ui
    public static ForgeConfigSpec.ConfigValue<String> MANA_BAR_POSITION, MANA_BAR_ORIENTATION;
    
    public static class Positions {
    	public static final String BOTTOM_LEFT = "bottomLeft",
    							   LEFT = "left",
    							   TOP_LEFT = "topLeft",
    							   TOP = "top",
    							   TOP_RIGHT = "topRight",
    							   RIGHT = "right",
    							   BOTTOM_RIGHT = "bottomRight";
    	public static List<String> VALUES = ImmutableList.of(BOTTOM_LEFT, LEFT, TOP_LEFT, TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT);
    }
    
    public static class Orientations {
    	public static final String HORIZONTAL = "horizontal",
    							   VERTICAL = "vertical",
    							   DEFAULT = "default";
    	
    	public static final List<String> VALUES = ImmutableList.of(HORIZONTAL, VERTICAL, DEFAULT);
    }

    public ClientConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Graphics settings").push("graphics");
        BETTER_LAYERING = builder.comment("Enable better particle/effect layering.",
            	"Fixes particles and effects rendering behind clouds and weather.",
            	"NOTE: Does NOT work with fabulous graphics mode.")
            .define("betterLayering", true);
        builder.pop();
        builder.comment("UI settings").push("ui");
        MANA_BAR_POSITION = builder.comment("Onscreen positioning of the magic power meter.")
        	.defineInList("manaBarPosition", "top", Positions.VALUES);
        MANA_BAR_ORIENTATION = builder.comment("Orientation of the magic power meter.")
        	.defineInList("manaBarOrientation", "default", Orientations.VALUES);
        builder.pop();
    }

    public static final ClientConfig INSTANCE;
    public static final ForgeConfigSpec SPEC;

    static {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        SPEC = specPair.getRight();
        INSTANCE = specPair.getLeft();
    }
}
