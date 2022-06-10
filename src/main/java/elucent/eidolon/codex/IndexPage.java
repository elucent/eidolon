package elucent.eidolon.codex;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import elucent.eidolon.Eidolon;
import elucent.eidolon.spell.KnowledgeUtil;
import elucent.eidolon.spell.Sign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class IndexPage extends Page {
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Eidolon.MODID, "textures/gui/codex_index_page.png");
    IndexEntry[] entries;

    public static class IndexEntry {
        Chapter chapter;
        ItemStack icon;

        public IndexEntry(Chapter chapter, ItemStack icon) {
            this.chapter = chapter;
            this.icon = icon;
        }

        @OnlyIn(Dist.CLIENT)
        public boolean isUnlocked() {
            return true;
        }
    }

    public static class SignLockedEntry extends IndexEntry {
        Sign[] signs;
        public SignLockedEntry(Chapter chapter, ItemStack icon, Sign... signs) {
            super(chapter, icon);
            this.signs = signs;
        }

        @Override
        public boolean isUnlocked() {
            for (Sign sign : signs) if (!KnowledgeUtil.knowsSign(Eidolon.proxy.getPlayer(), sign)) return false;
            return true;
        }
    }

    public static class FactLockedEntry extends IndexEntry {
        ResourceLocation[] facts;
        public FactLockedEntry(Chapter chapter, ItemStack icon, ResourceLocation... facts) {
            super(chapter, icon);
            this.facts = facts;
        }

        @Override
        public boolean isUnlocked() {
            for (ResourceLocation fact : facts) if (!KnowledgeUtil.knowsFact(Eidolon.proxy.getPlayer(), fact)) return false;
            return true;
        }
    }

    public IndexPage(IndexEntry... pages) {
        super(BACKGROUND);
        this.entries = pages;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean click(CodexGui gui, int x, int y, int mouseX, int mouseY) {
        for (int i = 0; i < entries.length; i ++) if (entries[i].isUnlocked()) {
            if (mouseX >= x + 2 && mouseX <= x + 124 && mouseY >= y + 8 + i * 20 && mouseY <= y + 26 + i * 20) {
                gui.changeChapter(entries[i].chapter);
                Minecraft.getInstance().player.playNotifySound(SoundEvents.UI_BUTTON_CLICK, SoundSource.NEUTRAL, 1.0f, 1.0f);
                return true;
            }
        }
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(CodexGui gui, PoseStack mStack, int x, int y, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, BACKGROUND);
        for (int i = 0; i < entries.length; i ++) {
            gui.blit(mStack, x + 1, y + 7 + i * 20, 128, entries[i].isUnlocked() ? 0 : 96, 122, 18);
        }

        for (int i = 0; i < entries.length; i ++) if (entries[i].isUnlocked()) {
            Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(entries[i].icon, x + 2, y + 8 + i * 20);
            drawText(gui, mStack, I18n.get(entries[i].chapter.titleKey), x + 24, y + 20 + i * 20 - Minecraft.getInstance().font.lineHeight);
        }
    }
}
