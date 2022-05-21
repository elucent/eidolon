package elucent.eidolon.deity;

import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

import elucent.eidolon.capability.IReputation;
import elucent.eidolon.research.Research;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.util.KnowledgeUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;

public abstract class Deity {
    ResourceLocation id;
    int red, green, blue;

    public Deity(ResourceLocation id, int red, int green, int blue) {
        this.id = id;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public float getRed() {
        return red / 255.0f;
    }

    public float getGreen() {
        return green / 255.0f;
    }

    public float getBlue() {
        return blue / 255.0f;
    }

    public ResourceLocation getId() {
        return id;
    }
    
    public interface StageRequirement {
    	boolean isMet(Player player);
    }
    
    public class ResearchRequirement implements StageRequirement {
    	Research r;
    	public ResearchRequirement(Research r) {
    		this.r = r;
    	}
    	
    	@Override
    	public boolean isMet(Player player) {
    		return KnowledgeUtil.knowsResearch(player, r.getRegistryName());
    	}
    }
    
    public class SignRequirement implements StageRequirement {
    	Sign sign;
    	public SignRequirement(Sign sign) {
    		this.sign = sign;
    	}
    	
    	@Override
    	public boolean isMet(Player player) {
    		return KnowledgeUtil.knowsSign(player, sign);
    	}
    }
    
    public class Stage {
    	ResourceLocation id;
    	int rep; // Required reputation to meet this stage.
    	List<StageRequirement> reqs; // Requirements to meet this stage.
    	boolean major; // Whether or not this is considered a major stage.
    	
    	public Stage(ResourceLocation id, int rep, boolean major) {
    		this.id = id;
    		this.rep = rep;
    		this.major = major;
    	}
    	
    	boolean satisfiedBy(Player player) {
    		for (StageRequirement req : reqs) {
    			
    		}
    		return true;
    	}
    }
    
    public class Progression {
    	TreeMap<Integer, Stage> steps;
    	int max;
    	
    	public Progression(Stage... stages) {
    		this.steps = new TreeMap<Integer, Stage>();
    		for (Stage s : stages) this.steps.put(s.rep, s);
    		max = this.steps.lastKey();
    	}
    	
    	public Stage next(double rep) {
    		return steps.ceilingEntry((int)(rep + 0.5f)).getValue();
    	}
    	
    	public Stage last(double rep) {
    		return steps.floorEntry((int)rep).getValue();
    	}
    	
    	public boolean progress(IReputation rep, Player player) {
    		double level = rep.getReputation(player, Deity.this.getId());
    		if (level >= max) return false; // Can't progress past max.
    		Stage s = next(level);
    		if (s.satisfiedBy(player)) return true;
    		else if (level > s.rep) {
    			rep.setReputation(player, Deity.this.getId(), s.rep);
    		}
    		return false;
    	}
    	
    	public void regress(IReputation rep, Player player) {
    		
    	}
    }

    public abstract void onReputationUnlock(Player player, IReputation rep, ResourceLocation lock);
    public abstract void onReputationChange(Player player, IReputation rep, double prev, double current);
}
