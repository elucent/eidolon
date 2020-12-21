package elucent.eidolon.recipe;

import elucent.eidolon.ritual.IRitualItemFocus;
import elucent.eidolon.ritual.MultiItemSacrifice;
import elucent.eidolon.ritual.Ritual;
import elucent.eidolon.ritual.RitualRegistry;
import elucent.eidolon.tile.CrucibleTileEntity;
import elucent.eidolon.tile.CrucibleTileEntity.CrucibleStep;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CrucibleRecipe {
    List<Step> steps = new ArrayList<>();
    ResourceLocation registryName;
    ItemStack result;

    public ItemStack getResult() {
        return result;
    }

    public static class Step {
        public List<Object> matches = new ArrayList<>();
        public int stirs;

        public Step(int stirs, List<Object> matches) {
            this.stirs = stirs;
            this.matches.addAll(matches);
        }
    };

    public CrucibleRecipe(ItemStack result) {
        this.result = result;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    public CrucibleRecipe setRegistryName(String domain, String path) {
        this.registryName = new ResourceLocation(domain, path);
        return this;
    }

    public CrucibleRecipe setRegistryName(ResourceLocation registryName) {
        this.registryName = registryName;
        return this;
    }

    public CrucibleRecipe addStep(Object... matches) {
        addStirringStep(0, matches);
        return this;
    }

    public CrucibleRecipe addStep(int stirs) {
        addStirringStep(stirs, new Object[]{});
        return this;
    }

    public CrucibleRecipe addStirringStep(int stirs, Object... matches) {
        steps.add(new Step(stirs, Arrays.asList(matches)));
        return this;
    }

    static boolean matches(Object match, ItemStack sacrifice) {
        if (match instanceof ItemStack) {
            if (ItemStack.areItemStacksEqual((ItemStack)match, sacrifice)) return true;
        }
        else if (match instanceof Item) {
            if ((Item)match == sacrifice.getItem()) return true;
        }
        else if (match instanceof Block) {
            if (((Block)match).asItem() == sacrifice.getItem()) return true;
        }
        else if (match instanceof ITag) {
            if (((ITag<Item>)match).contains(sacrifice.getItem())) return true;
        }
        return false;
    }

    public boolean matches(List<CrucibleStep> items) {
        if (steps.size() != items.size()) return false;

        List<Object> matchList = new ArrayList<>();
        List<ItemStack> itemList = new ArrayList<>();

        for (int i = 0; i < steps.size(); i ++) {
            Step correct = steps.get(i);
            CrucibleStep provided = items.get(i);
            if (correct.stirs != provided.getStirs()) return false;

            matchList.clear();
            itemList.clear();
            matchList.addAll(correct.matches);
            itemList.addAll(provided.getContents());

            for (int j = 0; j < matchList.size(); j ++) {
                for (int k = 0; k < itemList.size(); k ++) {
                    if (matches(matchList.get(j), itemList.get(k))) {
                        matchList.remove(j --);
                        itemList.remove(k --);
                        break;
                    }
                }
            }

            if (matchList.size() != 0) return false;
        }

        return true;
    }
}
