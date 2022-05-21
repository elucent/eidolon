package elucent.eidolon.spell;

import net.minecraft.resources.ResourceLocation;

public abstract class Rune {
    ResourceLocation key, sprite;
	ResourceLocation registryName;
	
	public Rune(ResourceLocation registryName) {
		this.key = registryName;
		this.sprite = new ResourceLocation(key.getNamespace(), "rune/" + key.getPath());
	}
	
	public Rune(ResourceLocation registryName, ResourceLocation sprite) {
		this.key = registryName;
		this.sprite = sprite;
	}
	
	public ResourceLocation getRegistryName() {
		return key;
	}

    public ResourceLocation getSprite() {
        return sprite;
    }
	
	public enum RuneResult {
		PASS, FAIL
	}
	
	public abstract RuneResult doEffect(SignSequence seq);

    @Override
    public boolean equals(Object other) {
        return other instanceof Sign && ((Sign)other).key.equals(key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
