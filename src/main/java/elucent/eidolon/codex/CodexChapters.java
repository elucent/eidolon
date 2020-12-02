package elucent.eidolon.codex;

import elucent.eidolon.Registry;
import elucent.eidolon.codex.IndexPage.IndexEntry;
import elucent.eidolon.codex.SignIndexPage.SignEntry;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import elucent.eidolon.util.ColorUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class CodexChapters {
    static List<Category> categories = new ArrayList<>();
    static Category NATURE, SIGNS;

    static Chapter NATURE_INDEX, MONSTERS, ORES, PEWTER, ENCHANTED_ASH,
        RITUALS_INDEX, BRAZIER, ITEM_PROVIDERS, CRYSTAL_RITUAL, SUMMON_RITUAL, ALLURE_RITUAL, DECEIT_RITUAL, TIME_RITUALS, PURIFY_RITUAL, SANGUINE_RITUAL,
        ARTIFICE_INDEX, CRUCIBLE, SOUL_GEMS,
        SIGNS_INDEX, WICKED_SIGN, SACRED_SIGN;

    public static void init() {
        MONSTERS = new Chapter(
            "eidolon.codex.chapter.monsters",
            new TitlePage("eidolon.codex.page.monsters.zombie_brute"),
            new EntityPage(Registry.ZOMBIE_BRUTE.get()),
            new TitlePage("eidolon.codex.page.monsters.wraith"),
            new EntityPage(Registry.WRAITH.get()),
            new TitlePage("eidolon.codex.page.monsters.chilled")
        );

        ORES = new Chapter(
            "eidolon.codex.chapter.ores",
            new TitlePage("eidolon.codex.page.ores.lead_ore"),
            new SmeltingPage(new ItemStack(Registry.LEAD_INGOT.get()), new ItemStack(Registry.LEAD_ORE.get())),
            new CraftingPage(new ItemStack(Registry.LEAD_BLOCK.get()),
                new ItemStack(Registry.LEAD_INGOT.get()), new ItemStack(Registry.LEAD_INGOT.get()), new ItemStack(Registry.LEAD_INGOT.get()),
                new ItemStack(Registry.LEAD_INGOT.get()), new ItemStack(Registry.LEAD_INGOT.get()), new ItemStack(Registry.LEAD_INGOT.get()),
                new ItemStack(Registry.LEAD_INGOT.get()), new ItemStack(Registry.LEAD_INGOT.get()), new ItemStack(Registry.LEAD_INGOT.get())),
            new CraftingPage(new ItemStack(Registry.LEAD_NUGGET.get(), 9), new ItemStack(Registry.LEAD_INGOT.get()))
        );

        PEWTER = new Chapter(
            "eidolon.codex.chapter.pewter",
            new TitlePage("eidolon.codex.page.pewter"),
            new CraftingPage(new ItemStack(Registry.PEWTER_BLEND.get(), 2),
                new ItemStack(Registry.LEAD_INGOT.get()), new ItemStack(Items.IRON_INGOT)),
            new SmeltingPage(new ItemStack(Registry.PEWTER_INGOT.get()), new ItemStack(Registry.PEWTER_BLEND.get())),
            new CraftingPage(new ItemStack(Registry.PEWTER_BLOCK.get()),
                new ItemStack(Registry.PEWTER_INGOT.get()), new ItemStack(Registry.PEWTER_INGOT.get()), new ItemStack(Registry.PEWTER_INGOT.get()),
                new ItemStack(Registry.PEWTER_INGOT.get()), new ItemStack(Registry.PEWTER_INGOT.get()), new ItemStack(Registry.PEWTER_INGOT.get()),
                new ItemStack(Registry.PEWTER_INGOT.get()), new ItemStack(Registry.PEWTER_INGOT.get()), new ItemStack(Registry.PEWTER_INGOT.get())),
            new CraftingPage(new ItemStack(Registry.PEWTER_NUGGET.get(), 9), new ItemStack(Registry.PEWTER_INGOT.get()))
        );

        ENCHANTED_ASH = new Chapter(
            "eidolon.codex.chapter.enchanted_ash",
            new TitlePage("eidolon.codex.page.enchanted_ash"),
            new SmeltingPage(new ItemStack(Registry.ENCHANTED_ASH.get(), 2), new ItemStack(Items.BONE))
        );

        NATURE_INDEX = new Chapter(
            "eidolon.codex.chapter.nature_index",
            new TitledIndexPage("eidolon.codex.page.nature_index.0",
                new IndexEntry(MONSTERS, new ItemStack(Registry.TATTERED_CLOTH.get())),
                new IndexEntry(ORES, new ItemStack(Registry.LEAD_ORE.get())),
                new IndexEntry(PEWTER, new ItemStack(Registry.PEWTER_INGOT.get())),
                new IndexEntry(ENCHANTED_ASH, new ItemStack(Registry.ENCHANTED_ASH.get()))
            )
        );

        categories.add(NATURE = new Category(
            "nature",
            new ItemStack(Registry.ZOMBIE_HEART.get()),
            ColorUtil.packColor(255, 89, 143, 76),
            NATURE_INDEX
        ));

        WICKED_SIGN = new Chapter(
            "eidolon.codex.chapter.wicked_sign",
            new TitlePage("eidolon.codex.page.wicked_sign"),
            new SignPage(Signs.WICKED_SIGN)
        );

        SACRED_SIGN = new Chapter(
            "eidolon.codex.chapter.sacred_sign",
            new TitlePage("eidolon.codex.page.sacred_sign"),
            new SignPage(Signs.SACRED_SIGN)
        );

        SIGNS_INDEX = new Chapter(
            "eidolon.codex.chapter.signs_index",
            new SignIndexPage(
                new SignEntry(WICKED_SIGN, Signs.WICKED_SIGN),
                new SignEntry(SACRED_SIGN, Signs.SACRED_SIGN)
            )
        );

        categories.add(SIGNS = new Category(
            "signs",
            new ItemStack(Registry.UNHOLY_SYMBOL.get()),
            ColorUtil.packColor(255, 94, 90, 219),
            SIGNS_INDEX
        ));
    }
}
