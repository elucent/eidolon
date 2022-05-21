package elucent.eidolon.capability;

import java.util.HashSet;
import java.util.Set;

import elucent.eidolon.Config;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.common.util.INBTSerializable;

public class SoulImpl implements ISoul, INBTSerializable<CompoundTag> {
    float maxMagic, magic;
    float maxEtherealHealth, etherealHealth;

	@Override
	public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("maxEtherealHealth", maxEtherealHealth);
        tag.putFloat("etherealHealth", etherealHealth);
        tag.putFloat("maxMagic", maxMagic);
        tag.putFloat("magic", magic);
        return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
        maxEtherealHealth = nbt.contains("maxEtherealHealth") ? nbt.getFloat("maxEtherealHealth") : 0;
        etherealHealth = nbt.contains("etherealHealth") ? nbt.getFloat("etherealHealth") : 0;
        maxMagic = nbt.contains("maxMagic") ? nbt.getFloat("maxMagic") : 0;
        magic = nbt.contains("magic") ? nbt.getFloat("magic") : 0;
	}

	@Override
	public boolean hasEtherealHealth() {
		return maxEtherealHealth > 0;
	}

	@Override
	public float getMaxEtherealHealth() {
		return maxEtherealHealth;
	}

	@Override
	public float getEtherealHealth() {
		return etherealHealth;
	}

	@Override
	public boolean hasMagic() {
		return maxMagic > 0;
	}

	@Override
	public float getMaxMagic() {
		return maxMagic;
	}

	@Override
	public float getMagic() {
		return magic;
	}

	@Override
	public void setEtherealHealth(float health) {
		this.etherealHealth = Mth.clamp(health, 0, maxEtherealHealth);
	}

	@Override
	public void setMagic(float magic) {
		this.magic = Mth.clamp(magic, 0, maxMagic);
	}

	@Override
	public void setMaxEtherealHealth(float max) {
		this.maxEtherealHealth = Mth.clamp(max, 0, Config.MAX_ETHEREAL_HEALTH.get());
		this.etherealHealth = Math.min(maxEtherealHealth, etherealHealth);
	}

	@Override
	public void setMaxMagic(float max) {
		this.maxMagic = Math.max(0, max);
		this.magic = Math.min(maxMagic, magic);
	}
}
