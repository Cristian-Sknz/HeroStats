package me.skiincraft.discord.herostats;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.security.auth.login.LoginException;

import me.skiincraft.api.paladins.Paladins;
import me.skiincraft.api.paladins.entity.Session;
import me.skiincraft.discord.core.plugin.OusuPlugin;
import me.skiincraft.discord.core.plugin.PluginManager;
import me.skiincraft.discord.core.reactions.ReactionUtil;
import me.skiincraft.discord.core.sqlobjects.TableBuilder;
import me.skiincraft.discord.core.sqlobjects.TableBuilder.TableValues;
import me.skiincraft.discord.herostats.commands.ChampionCommand;
import me.skiincraft.discord.herostats.commands.ClassStatsCommand;
import me.skiincraft.discord.herostats.commands.DeckCommand;
import me.skiincraft.discord.herostats.commands.PartidaCommand;
import me.skiincraft.discord.herostats.commands.StatusCommand;
import me.skiincraft.discord.herostats.session.SessionDB;
import net.dv8tion.jda.api.entities.Emote;

public class HeroStatsBot extends OusuPlugin {

	private static HeroStatsBot herostats;
	private static Paladins paladins;

	public static Paladins getPaladins() {
		return paladins;
	}

	public static HeroStatsBot getMain() {
		return herostats;
	}

	public static List<Emote> emotes = new ArrayList<>();
	public static List<ReactionUtil> simples = new ArrayList<>();

	public static Emote getEmoteByName(String emotename) {
		return emotes.stream().filter(o -> o.getName().equalsIgnoreCase(emotename)).findAny().orElse(null);
	}
	
	@Override
	public void onEnable() {
		TableBuilder tableBuilder = new TableBuilder("sessions");
		
		tableBuilder.addColumn("order", TableValues.VARCHAR);
		tableBuilder.addColumn("session", TableValues.VARCHAR);
		tableBuilder.addColumn("date", TableValues.VARCHAR);
		
		tableBuilder.setIdAutoIncrement(false);
		
		getPlugin().getSQLite().createTable(tableBuilder.build());
		herostats = this;
		SessionDB sessiondb = new SessionDB();
		paladins = new Paladins(DEVID, "TOKEN", sessiondb.getLastSession());
		paladins.getSessionsCache().get(0).getRequester().refreshChampions();
		sessiondb.setSession(paladins.getSessionsCache().get(0).getSessionId());
		paladins.getSessionsCache().get(0).setOnValidation(new Consumer<Session>() {
			public void accept(Session t) {
				sessiondb.setSession(t.getSessionId());
			}
		});
		
		PluginManager.getPluginManager().registerCommands(new PartidaCommand());
		PluginManager.getPluginManager().registerCommands(new StatusCommand());
		PluginManager.getPluginManager().registerCommands(new ChampionCommand());
		PluginManager.getPluginManager().registerCommands(new DeckCommand());
		PluginManager.getPluginManager().registerCommands(new ClassStatsCommand());
		
		try {
			startbot();
		} catch (LoginException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		//emotes = getShardManager().getGuildById("719393220756242524").getEmotes();
	}

}
