package elucent.eidolon.research;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import elucent.eidolon.Eidolon;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

public class Researches {
    static Map<ResourceLocation, Research> researches = new HashMap<>();
    static Multimap<ResourceLocation, Research> blockResearches = HashMultimap.create();
    static Multimap<ResourceLocation, Research> entityResearches = HashMultimap.create();
    static List<Function<Random, ResearchTask>> taskPool = new ArrayList<>();

    public static Research register(Research r, Object... sources) {
        researches.put(r.getRegistryName(), r);
        for (Object o : sources) {
        	if (o instanceof Block b) {
        		blockResearches.put(b.getRegistryName(), r);
        	}
        	else if (o instanceof EntityType<?> e) {
        		entityResearches.put(e.getRegistryName(), r);
        	}
        }
        return r;
    }
    
    public static void addTask(Function<Random, ResearchTask> task) {
    	taskPool.add(task);
    }
    
    @Nullable
    public static Collection<Research> getBlockResearches(Block b) {
    	return blockResearches.get(b.getRegistryName());
    }
    
    @Nullable
    public static Collection<Research> getEntityResearches(Entity e) {
    	return entityResearches.get(e.getType().getRegistryName());
    }

    public static Collection<Research> getResearches() {
        return researches.values();
    }

    @Nullable
    public static Research find(ResourceLocation location) {
        return researches.getOrDefault(location, null);
    }
    
    public static ResearchTask getRandomTask(Random random) {
    	return taskPool.get(random.nextInt(taskPool.size())).apply(random);
    }
    
    public static void init() {
    	addTask(ResearchTask.ScrivenerItems::new);
    	addTask(ResearchTask.ScrivenerItems::new);
    	addTask(ResearchTask.ScrivenerItems::new);
    	addTask(ResearchTask.ScrivenerItems::new);
    	addTask(ResearchTask.XP::new);
    	addTask(ResearchTask.XP::new);
    	register(new Research(new ResourceLocation(Eidolon.MODID, "gluttony"), 5), EntityType.PIG);
    }
}
