package elucent.eidolon.ritual;

import elucent.eidolon.Eidolon;
import elucent.eidolon.Registry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RitualRegistry {
    static Map<ResourceLocation, Ritual> rituals = new HashMap<>();
    static Map<Object, Ritual> matches = new HashMap<>();

    public static void register(ItemStack sacrifice, Ritual ritual) {
        ResourceLocation name = ritual.getRegistryName();
        assert name != null;
        rituals.put(name, ritual);
        matches.put(sacrifice, ritual);
    }

    public static void register(Item sacrifice, Ritual ritual) {
        ResourceLocation name = ritual.getRegistryName();
        assert name != null;
        rituals.put(name, ritual);
        matches.put(sacrifice, ritual);
    }

    public static void register(Block sacrifice, Ritual ritual) {
        ResourceLocation name = ritual.getRegistryName();
        assert name != null;
        rituals.put(name, ritual);
        matches.put(Item.getItemFromBlock(sacrifice), ritual);
    }

    public static void register(ITag<Item> sacrifice, Ritual ritual) {
        ResourceLocation name = ritual.getRegistryName();
        assert name != null;
        rituals.put(name, ritual);
        matches.put(sacrifice, ritual);
    }

    public static void register(MultiItemSacrifice sacrifice, Ritual ritual) {
        ResourceLocation name = ritual.getRegistryName();
        assert name != null;
        rituals.put(name, ritual);
        matches.put(sacrifice, ritual);
    }

    public static Ritual find(ResourceLocation name) {
        return rituals.get(name);
    }

    static boolean matches(World world, BlockPos pos, Object match, ItemStack sacrifice) {
        if (match instanceof ItemStack) {
            if (ItemStack.areItemStacksEqual((ItemStack)match, sacrifice)) return true;
        }
        else if (match instanceof Item) {
            if ((Item)match == sacrifice.getItem()) return true;
        }
        else if (match instanceof ITag) {
            if (((ITag<Item>)match).contains(sacrifice.getItem())) return true;
        }
        else if (match instanceof MultiItemSacrifice) {
            // check main item first, avoid complicated work
            if (!matches(world, pos, ((MultiItemSacrifice)match).main, sacrifice)) return false;

            List<Object> matches = new ArrayList<>();
            matches.addAll(((MultiItemSacrifice)match).items);
            List<ItemStack> items = new ArrayList<>();
            List<IRitualItemFocus> foci = Ritual.getTilesWithinAABB(IRitualItemFocus.class, world, Ritual.getDefaultBounds(pos));
            for (IRitualItemFocus focus : foci) items.add(focus.provide());
            if (items.size() != matches.size()) return false;
            for (int i = 0; i < matches.size(); i ++) {
                int before = matches.size();
                for (int j = 0; j < items.size(); j ++) {
                    Object m = matches.get(i);
                    ItemStack item = items.get(j);
                    if (RitualRegistry.matches(world, pos, m, item)) {
                        matches.remove(i --);
                        items.remove(j --);
                        break;
                    }
                }
                if (matches.size() == before) return false; // failed to satisfy match with any item
            }
            return matches.size() == 0; // all matches satisfied
        }
        return false;
    }

    public static Ritual find(World world, BlockPos pos, ItemStack sacrifice) {
        for (Entry<Object, Ritual> entry : matches.entrySet()) {
            if (matches(world, pos, entry.getKey(), sacrifice)) return entry.getValue();
        }
        return null;
    }

    public static void init() {
        register(Items.BONE_MEAL, new CrystalRitual().setRegistryName(Eidolon.MODID, "crystal")
            .addRequirement(new ItemRequirement(Tags.Items.DUSTS_REDSTONE))
            .addRequirement(new ItemRequirement(Tags.Items.DUSTS_REDSTONE)));

        // summons
        register(new MultiItemSacrifice(Items.CHARCOAL, Items.ROTTEN_FLESH), new SummonRitual(EntityType.ZOMBIE).setRegistryName(Eidolon.MODID, "summon_zombie")
            .addRequirement(new ItemRequirement(Items.ROTTEN_FLESH))
            .addRequirement(new ItemRequirement(Items.ROTTEN_FLESH))
            .addRequirement(new ItemRequirement(Items.ROTTEN_FLESH))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get())));
        register(new MultiItemSacrifice(Items.CHARCOAL, Items.BONE), new SummonRitual(EntityType.SKELETON).setRegistryName(Eidolon.MODID, "summon_skeleton")
            .addRequirement(new ItemRequirement(Items.BONE))
            .addRequirement(new ItemRequirement(Items.BONE))
            .addRequirement(new ItemRequirement(Items.BONE))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get())));
        register(new MultiItemSacrifice(Items.CHARCOAL, Items.PHANTOM_MEMBRANE), new SummonRitual(EntityType.PHANTOM).setRegistryName(Eidolon.MODID, "summon_phantom")
            .addRequirement(new ItemRequirement(Items.PHANTOM_MEMBRANE))
            .addRequirement(new ItemRequirement(Items.PHANTOM_MEMBRANE))
            .addRequirement(new ItemRequirement(Items.PHANTOM_MEMBRANE))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get())));
        register(new MultiItemSacrifice(Items.CHARCOAL, Items.COAL), new SummonRitual(EntityType.WITHER_SKELETON).setRegistryName(Eidolon.MODID, "summon_wither_skeleton")
            .addRequirement(new ItemRequirement(Items.BONE))
            .addRequirement(new ItemRequirement(Items.BONE))
            .addRequirement(new ItemRequirement(Items.COAL))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get())));
        register(new MultiItemSacrifice(Items.CHARCOAL, Tags.Items.SAND), new SummonRitual(EntityType.HUSK).setRegistryName(Eidolon.MODID, "summon_husk")
            .addRequirement(new ItemRequirement(Items.ROTTEN_FLESH))
            .addRequirement(new ItemRequirement(Items.ROTTEN_FLESH))
            .addRequirement(new ItemRequirement(Tags.Items.SAND))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get())));
        register(new MultiItemSacrifice(Items.CHARCOAL, Tags.Items.GEMS_PRISMARINE), new SummonRitual(EntityType.DROWNED).setRegistryName(Eidolon.MODID, "summon_drowned")
            .addRequirement(new ItemRequirement(Items.ROTTEN_FLESH))
            .addRequirement(new ItemRequirement(Items.ROTTEN_FLESH))
            .addRequirement(new ItemRequirement(Tags.Items.GEMS_PRISMARINE))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get())));
        register(new MultiItemSacrifice(Items.CHARCOAL, Items.SNOWBALL), new SummonRitual(EntityType.STRAY).setRegistryName(Eidolon.MODID, "summon_stray")
            .addRequirement(new ItemRequirement(Items.BONE))
            .addRequirement(new ItemRequirement(Items.BONE))
            .addRequirement(new ItemRequirement(Items.SNOWBALL))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get())));
        register(new MultiItemSacrifice(Items.CHARCOAL, Registry.TATTERED_CLOTH.get()), new SummonRitual(Registry.WRAITH.get()).setRegistryName(Eidolon.MODID, "summon_wraith")
            .addRequirement(new ItemRequirement(Registry.TATTERED_CLOTH.get()))
            .addRequirement(new ItemRequirement(Registry.TATTERED_CLOTH.get()))
            .addRequirement(new ItemRequirement(Registry.TATTERED_CLOTH.get()))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get())));

        register(Tags.Items.GEMS_EMERALD, new DeceitRitual().setRegistryName(Eidolon.MODID, "deceit")
            .addRequirement(new ItemRequirement(Tags.Items.GEMS_EMERALD))
            .addRequirement(new ItemRequirement(Items.FERMENTED_SPIDER_EYE))
            .addRequirement(new ItemRequirement(Tags.Items.MUSHROOMS))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get()))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get())));

        register(Items.ROSE_BUSH, new AllureRitual().setRegistryName(Eidolon.MODID, "allure")
            .addRequirement(new ItemRequirement(Items.GOLDEN_APPLE))
            .addRequirement(new ItemRequirement(Items.RED_DYE))
            .addRequirement(new ItemRequirement(Items.RED_DYE))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get()))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get())));

        register(Items.SUNFLOWER, new DaylightRitual().setRegistryName(Eidolon.MODID, "daylight")
            .addRequirement(new ItemRequirement(Items.CHARCOAL))
            .addRequirement(new ItemRequirement(Items.WHEAT_SEEDS))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get()))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get())));

        register(Tags.Items.DYES_BLACK, new MoonlightRitual().setRegistryName(Eidolon.MODID, "moonlight")
            .addRequirement(new ItemRequirement(Items.SNOWBALL))
            .addRequirement(new ItemRequirement(Items.SPIDER_EYE))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get()))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get())));

        register(Items.GLISTERING_MELON_SLICE, new PurifyRitual().setRegistryName(Eidolon.MODID, "purify")
            .addRequirement(new ItemRequirement(Registry.ENCHANTED_ASH.get()))
            .addRequirement(new ItemRequirement(Registry.ENCHANTED_ASH.get()))
            .addRequirement(new ItemRequirement(Registry.ENCHANTED_ASH.get()))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get()))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get()))
            .addRequirement(new ItemRequirement(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.HEALING))));

        register(new MultiItemSacrifice(Items.WOODEN_AXE, Registry.BASIC_AMULET.get()), new InfuseRitual(new ItemStack(Registry.VOID_AMULET.get()), 190, 212, 184).setRegistryName(Eidolon.MODID, "infuse_void_amulet")
            .addRequirement(new ItemRequirement(Registry.BASIC_AMULET.get()))
            .addRequirement(new ItemRequirement(Registry.PEWTER_INLAY.get()))
            .addRequirement(new ItemRequirement(Registry.PEWTER_INLAY.get()))
            .addRequirement(new ItemRequirement(Items.CRYING_OBSIDIAN))
            .addRequirement(new ItemRequirement(Items.OBSIDIAN))
            .addRequirement(new ItemRequirement(Items.OBSIDIAN))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get())));

        register(new MultiItemSacrifice(Items.WOODEN_AXE, Items.IRON_CHESTPLATE), new InfuseRitual(new ItemStack(Registry.WARDED_MAIL.get()), 190, 212, 184).setRegistryName(Eidolon.MODID, "infuse_warded_mail")
            .addRequirement(new ItemRequirement(Items.IRON_CHESTPLATE))
            .addRequirement(new ItemRequirement(Registry.PEWTER_INLAY.get()))
            .addRequirement(new ItemRequirement(Registry.PEWTER_INLAY.get()))
            .addRequirement(new ItemRequirement(Registry.PEWTER_INLAY.get()))
            .addRequirement(new ItemRequirement(Registry.PEWTER_INLAY.get()))
            .addRequirement(new ItemRequirement(Registry.LESSER_SOUL_GEM.get()))
            .addRequirement(new ItemRequirement(Registry.ENCHANTED_ASH.get()))
            .addRequirement(new ItemRequirement(Registry.ENCHANTED_ASH.get())));

        register(new MultiItemSacrifice(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.HARMING), Items.IRON_SWORD), new SanguineRitual(new ItemStack(Registry.SAPPING_SWORD.get())).setRegistryName(Eidolon.MODID, "sanguine_sapping_sword")
            .addRequirement(new ItemRequirement(Items.IRON_SWORD))
            .addRequirement(new ItemRequirement(Registry.SHADOW_GEM.get()))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get()))
            .addRequirement(new ItemRequirement(Registry.SOUL_SHARD.get()))
            .addRequirement(new ItemRequirement(Items.GHAST_TEAR))
            .addRequirement(new ItemRequirement(Items.NETHER_WART))
            .addRequirement(new ItemRequirement(Items.NETHER_WART))
            .addRequirement(new HealthRequirement(20)));

        register(new MultiItemSacrifice(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.HARMING), Registry.BASIC_AMULET.get()), new SanguineRitual(new ItemStack(Registry.SANGUINE_AMULET.get())).setRegistryName(Eidolon.MODID, "sanguine_sanguine_amulet")
            .addRequirement(new ItemRequirement(Registry.BASIC_AMULET.get()))
            .addRequirement(new ItemRequirement(Tags.Items.GEMS_DIAMOND))
            .addRequirement(new ItemRequirement(Tags.Items.DUSTS_REDSTONE))
            .addRequirement(new ItemRequirement(Tags.Items.DUSTS_REDSTONE))
            .addRequirement(new ItemRequirement(Tags.Items.DUSTS_REDSTONE))
            .addRequirement(new ItemRequirement(Tags.Items.DUSTS_REDSTONE))
            .addRequirement(new ItemRequirement(Registry.LESSER_SOUL_GEM.get()))
            .addRequirement(new HealthRequirement(40)));
    }
}
