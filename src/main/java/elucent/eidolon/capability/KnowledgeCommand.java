package elucent.eidolon.capability;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import elucent.eidolon.research.Research;
import elucent.eidolon.research.Researches;
import elucent.eidolon.spell.Rune;
import elucent.eidolon.spell.Runes;
import elucent.eidolon.spell.Sign;
import elucent.eidolon.spell.Signs;
import elucent.eidolon.util.KnowledgeUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.ExperienceCommand;
import net.minecraft.server.commands.KillCommand;
import net.minecraft.server.commands.MsgCommand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class KnowledgeCommand {
	public static class SignArgument implements ArgumentType<Sign> {
		private static final DynamicCommandExceptionType UNKNOWN = new DynamicCommandExceptionType((obj) -> new TranslatableComponent("argument.eidolon.sign.unknown", obj));
		
		public static Sign getSign(final CommandContext<?> context, final String name) {
			return context.getArgument(name, Sign.class);
		}
		
		@Override
		public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
			for (Sign s : Signs.getSigns()) 
				if (s.getRegistryName().toString().startsWith(builder.getRemainingLowerCase())) 
					builder.suggest(s.getRegistryName().toString());
			return builder.buildFuture();
	    }
		
		@Override
		public Sign parse(StringReader reader) throws CommandSyntaxException {
	        ResourceLocation rl = ResourceLocation.read(reader);
	        Sign s = Signs.find(rl);
	        if (s == null) throw UNKNOWN.create(rl.toString());
	        return s;
		}
	}
	
	public static class ResearchArgument implements ArgumentType<Research> {
		private static final DynamicCommandExceptionType UNKNOWN = new DynamicCommandExceptionType((obj) -> new TranslatableComponent("argument.eidolon.research.unknown", obj));
		
		public static Research getResearch(final CommandContext<?> context, final String name) {
			return context.getArgument(name, Research.class);
		}
		
		@Override
		public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
			for (Research r : Researches.getResearches()) 
				if (r.getRegistryName().toString().startsWith(builder.getRemainingLowerCase())) 
					builder.suggest(r.getRegistryName().toString());
			return builder.buildFuture();
	    }
		
		@Override
		public Research parse(StringReader reader) throws CommandSyntaxException {
	        ResourceLocation rl = ResourceLocation.read(reader);
	        Research r = Researches.find(rl);
	        if (r == null) throw UNKNOWN.create(rl.toString());
	        return r;
		}
	}
	
	public static class RuneArgument implements ArgumentType<Rune> {
		private static final DynamicCommandExceptionType UNKNOWN = new DynamicCommandExceptionType((obj) -> new TranslatableComponent("argument.eidolon.rune.unknown", obj));
		
		public static Rune getRune(final CommandContext<?> context, final String name) {
			return context.getArgument(name, Rune.class);
		}
		
		@Override
		public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
			for (Rune r : Runes.getRunes()) 
				if (r.getRegistryName().toString().startsWith(builder.getRemainingLowerCase())) 
					builder.suggest(r.getRegistryName().toString());
			return builder.buildFuture();
	    }
		
		@Override
		public Rune parse(StringReader reader) throws CommandSyntaxException {
	        ResourceLocation rl = ResourceLocation.read(reader);
	        Rune r = Runes.find(rl);
	        if (r == null) throw UNKNOWN.create(rl.toString());
	        return r;
		}
	}
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("knowledge").requires((player) -> {
			return player.hasPermission(2);
		}).then(Commands.argument("targets", EntityArgument.players())
			.then(Commands.literal("reset").then(Commands.literal("signs").executes((ctx) -> {
					return apply(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (player, sources) -> {
						player.getCapability(IKnowledge.INSTANCE).ifPresent((k) -> {
							KnowledgeUtil.resetSigns(player);
						});
					});
				}))
				.then(Commands.literal("facts").executes((ctx) -> {
					return apply(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (player, sources) -> {
						player.getCapability(IKnowledge.INSTANCE).ifPresent((k) -> {
							KnowledgeUtil.resetFacts(player);
						});
					});
				}))
				.then(Commands.literal("research").executes((ctx) -> {
					return apply(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (player, sources) -> {
						player.getCapability(IKnowledge.INSTANCE).ifPresent((k) -> {
							KnowledgeUtil.resetResearch(player);
						});
					});
				}))
				.then(Commands.literal("runes").executes((ctx) -> {
					return apply(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (player, sources) -> {
						player.getCapability(IKnowledge.INSTANCE).ifPresent((k) -> {
							KnowledgeUtil.resetRunes(player);
						});
					});
				}))
			)
			.then(Commands.literal("grant")
				.then(Commands.literal("sign").then(Commands.argument("sign", new SignArgument()).executes((ctx) -> {
					return apply(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (player, sources) -> {
						player.getCapability(IKnowledge.INSTANCE).ifPresent((k) -> {
							KnowledgeUtil.grantSign(player, SignArgument.getSign(ctx, "sign"));
						});
					});
				})))
				.then(Commands.literal("fact").then(Commands.argument("fact", ResourceLocationArgument.id()).executes((ctx) -> {
					return apply(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (player, sources) -> {
						player.getCapability(IKnowledge.INSTANCE).ifPresent((k) -> {
							KnowledgeUtil.grantFact(player, ResourceLocationArgument.getId(ctx, "sign"));
						});
					});
				})))
				.then(Commands.literal("research").then(Commands.argument("research", new ResearchArgument()).executes((ctx) -> {
					return apply(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (player, sources) -> {
						player.getCapability(IKnowledge.INSTANCE).ifPresent((k) -> {
							KnowledgeUtil.grantResearch(player, ResearchArgument.getResearch(ctx, "research").getRegistryName());
						});
					});
				})))
				.then(Commands.literal("rune").then(Commands.argument("rune", new RuneArgument()).executes((ctx) -> {
					return apply(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (player, sources) -> {
						player.getCapability(IKnowledge.INSTANCE).ifPresent((k) -> {
							KnowledgeUtil.grantRune(player, RuneArgument.getRune(ctx, "rune"));
						});
					});
				})))
			)
//			.then(Commands.literal("grant")
//				.then(Commands.literal("sign").then(Commands.argument("sign", new SignArgument()).executes((ctx) -> {
//					return apply(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (player, sources) -> {
//						player.getCapability(IKnowledge.INSTANCE).ifPresent((k) -> {
//							KnowledgeUtil.grantSign(player, SignArgument.getSign(ctx, "sign"));
//						});
//					});
//				})))
//				.then(Commands.literal("fact").then(Commands.argument("fact", ResourceLocationArgument.id()).executes((ctx) -> {
//					return apply(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (player, sources) -> {
//						player.getCapability(IKnowledge.INSTANCE).ifPresent((k) -> {
//							KnowledgeUtil.grantFact(player, ResourceLocationArgument.getId(ctx, "sign"));
//						});
//					});
//				})))
//				.then(Commands.literal("research").then(Commands.argument("research", new ResearchArgument()).executes((ctx) -> {
//					return apply(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (player, sources) -> {
//						player.getCapability(IKnowledge.INSTANCE).ifPresent((k) -> {
//							KnowledgeUtil.grantResearch(player, ResearchArgument.getResearch(ctx, "research").getRegistryName());
//						});
//					});
//				})))
//				.then(Commands.literal("rune").then(Commands.argument("rune", new RuneArgument()).executes((ctx) -> {
//					return apply(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (player, sources) -> {
//						player.getCapability(IKnowledge.INSTANCE).ifPresent((k) -> {
//							KnowledgeUtil.grantRune(player, RuneArgument.getRune(ctx, "rune"));
//						});
//					});
//				})))
//			)
			.then(Commands.literal("remove")
				.then(Commands.literal("sign").then(Commands.argument("sign", new SignArgument()).executes((ctx) -> {
					return apply(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (player, sources) -> {
						player.getCapability(IKnowledge.INSTANCE).ifPresent((k) -> {
							KnowledgeUtil.removeSign(player, SignArgument.getSign(ctx, "sign"));
						});
					});
				})))
				.then(Commands.literal("fact").then(Commands.argument("fact", ResourceLocationArgument.id()).executes((ctx) -> {
					return apply(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (player, sources) -> {
						player.getCapability(IKnowledge.INSTANCE).ifPresent((k) -> {
							KnowledgeUtil.removeFact(player, ResourceLocationArgument.getId(ctx, "sign"));
						});
					});
				})))
				.then(Commands.literal("research").then(Commands.argument("research", new ResearchArgument()).executes((ctx) -> {
					return apply(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (player, sources) -> {
						player.getCapability(IKnowledge.INSTANCE).ifPresent((k) -> {
							KnowledgeUtil.removeResearch(player, ResearchArgument.getResearch(ctx, "research").getRegistryName());
						});
					});
				})))
				.then(Commands.literal("rune").then(Commands.argument("rune", new RuneArgument()).executes((ctx) -> {
					return apply(ctx.getSource(), EntityArgument.getPlayers(ctx, "targets"), (player, sources) -> {
						player.getCapability(IKnowledge.INSTANCE).ifPresent((k) -> {
							KnowledgeUtil.removeRune(player, RuneArgument.getRune(ctx, "rune"));
						});
					});
				})))
			)
		));
   	}

   	private static int apply(CommandSourceStack sources, Collection<? extends Player> players, BiConsumer<Player, CommandSourceStack> action) {
   		for (Player player : players) {
   			action.accept(player, sources);
   		}

   		if (players.size() == 1) {
   			sources.sendSuccess(new TranslatableComponent("commands.eidolon.knowledge.success.single", players.iterator().next().getDisplayName()), true);
   		} else {
   			sources.sendSuccess(new TranslatableComponent("commands.eidolon.knowledge.success.multiple", players.size()), true);
   		}

   		return players.size();
   	}
}
