package me.skiincraft.discord.herostats.reactions;

import java.util.List;

import me.skiincraft.discord.core.reactions.Reaction;
import me.skiincraft.discord.core.reactions.ReactionUtil;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class FriendCommandReaction extends Reaction {

	public FriendCommandReaction() {
		super("friendreaction");
	}

	public List<ReactionUtil> listHistory() {
		return HistoryLists.friendsList;
	}

	public void execute(User user, TextChannel channel, ReactionEmote reactionEmote) {
		if (reactionEmote.getEmoji().equalsIgnoreCase("◀")) {
			getContext().changeEmbedBack(getUtils().getReactionObjects()[0]);
		}
		
		if (reactionEmote.getEmoji().equalsIgnoreCase("▶")) {
			getContext().changeEmbedNext(getUtils().getReactionObjects()[0]);
		}
	}

}
