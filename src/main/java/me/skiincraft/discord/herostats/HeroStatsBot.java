package me.skiincraft.discord.herostats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import me.skiincraft.api.paladins.Paladins;
import me.skiincraft.api.paladins.PaladinsBuilder;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.discord.core.plugin.OusuPlugin;
import me.skiincraft.discord.herostats.commands.*;
import me.skiincraft.discord.herostats.listeners.DeckChooser;
import me.skiincraft.discord.herostats.listeners.ReactionListeners;
import me.skiincraft.discord.herostats.reactions.PageReaction;
import me.skiincraft.discord.herostats.session.SessionConfiguration;

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

	public static Emote getEmoteByName(String emotename) {
		if (emotes.size() == 0)	emotes = Objects.requireNonNull(getMain().getShardManager().getGuildById("719393220756242524")).getEmotes();
		return emotes.stream().filter(o -> o.getName().equalsIgnoreCase(emotename)).findAny().orElse(null);
	}
	
	@Override
	public void onEnable() {		
		herostats = this;
		paladins = new PaladinsBuilder("YOUR-KEY", 1000).build();

		SessionConfiguration configuration = new SessionConfiguration(paladins);

		try {
			configuration.resumeSessions();
			paladins.getSessions().get(0).getEndPoint().getChampions(Language.Portuguese);
		} catch (IOException e) {
			e.printStackTrace();
		}

		getPlugin().getCommandManager().registerCommand(new StatusCommand());
		getPlugin().getCommandManager().registerCommand(new ChampionCommand());
		getPlugin().getCommandManager().registerCommand(new DeckCommand());
		getPlugin().getCommandManager().registerCommand(new ClassStatsCommand());
		getPlugin().getCommandManager().registerCommand(new SplitCommand());
		getPlugin().getCommandManager().registerCommand(new HelpCommand());
		getPlugin().getCommandManager().registerCommand(new PartidaCommand());
        getPlugin().getCommandManager().registerCommand(new LeaderboardCommand());
		getPlugin().getCommandManager().registerCommand(new PingCommand());
		getPlugin().getCommandManager().registerCommand(new DataUsedCommand());
		
		getPlugin().getEventManager().registerListener(new PageReaction());
		getPlugin().getEventManager().registerListener(new ReactionListeners());
		getPlugin().getEventManager().registerListener(new DeckChooser());

		getPlugin().addLanguage(new me.skiincraft.discord.core.configuration.Language(new Locale("pt", "BR")));
	}

}
