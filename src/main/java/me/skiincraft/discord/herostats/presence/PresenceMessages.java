package me.skiincraft.discord.herostats.presence;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.sharding.ShardManager;

public class PresenceMessages {

	public List<Activity> getMessages(ShardManager shardm) {
		List<Activity> l = new ArrayList<Activity>();

		int users = 0;
		int servers = shardm.getGuilds().size();
		l.add(Activity.listening("☕ | p!help for help."));
		for (Guild guild : shardm.getGuilds()) {
			users += guild.getMemberCount();
		}
		l.add(Activity.watching(users + " Usuarios Online!"));
		l.add(Activity.watching(servers + " Servidores"));
		l.add(Activity.watching("🆕 h!leaderboard to view leaderboard"));
		return l;
	}

}
