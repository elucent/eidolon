package elucent.eidolon.capability;

import elucent.eidolon.spell.Sign;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Set;

public interface IKnowledge extends INBTSerializable<CompoundTag> {
    boolean knowsSign(Sign sign);
    void addSign(Sign sign);
    Set<Sign> getKnownSigns();

    boolean knowsFact(ResourceLocation fact);
    void addFact(ResourceLocation fact);
    Set<ResourceLocation> getKnownFacts();
}
