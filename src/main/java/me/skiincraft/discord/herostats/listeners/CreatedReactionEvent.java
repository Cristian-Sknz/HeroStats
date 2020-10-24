package me.skiincraft.discord.herostats.listeners;

import me.skiincraft.discord.core.event.Event;
import me.skiincraft.discord.core.reactions.ReactionUtil;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class CreatedReactionEvent extends Event {
	
	private final TextChannel channel;
	private final User user;
	private final ReactionUtil reactionUtil;
	private final List<ReactionUtil> lista;
	
	public CreatedReactionEvent(TextChannel channel, User user, List<ReactionUtil> lista, ReactionUtil reactionUtil) {
		this.channel = channel;
		this.user = user;
		this.reactionUtil = reactionUtil;
		this.lista = lista;
	}
	
	public boolean delete() {
		return lista.remove(reactionUtil); 
	}
	
	public TextChannel getChannel() {
		return channel;
	}
	
	public ReactionUtil getReactionUtil() {
		return reactionUtil;
	}
	
	public User getUser() {
		return user;
	}
}

