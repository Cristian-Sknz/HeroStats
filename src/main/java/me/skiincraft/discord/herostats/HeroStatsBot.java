package me.skiincraft.discord.herostats;

import me.skiincraft.api.paladins.Paladins;
import me.skiincraft.api.paladins.PaladinsBuilder;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.discord.core.common.chooser.Chooser;
import me.skiincraft.discord.core.common.chooser.ChooserListeners;
import me.skiincraft.discord.core.common.reactions.ReactionListeners;
import me.skiincraft.discord.core.common.reactions.Reactions;
import me.skiincraft.discord.core.plugin.OusuPlugin;
import me.skiincraft.discord.herostats.commands.*;
import me.skiincraft.discord.herostats.session.SessionConfiguration;

import java.io.IOException;
import java.util.Locale;

public class HeroStatsBot extends OusuPlugin {

	private static HeroStatsBot herostats;
	private static Chooser chooser;
	private static Reactions reaction;
	private static Paladins paladins;

	public static Paladins getPaladins() {
		return paladins;
	}

	public static HeroStatsBot getMain() {
		return herostats;
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

		ChooserListeners listener = new ChooserListeners();
		chooser = Chooser.of(listener);
		getPlugin().getEventManager().registerListener(listener);

		ReactionListeners rlistener = new ReactionListeners();
		reaction = Reactions.of(rlistener);
		getPlugin().getEventManager().registerListener(rlistener);

		getPlugin().addLanguage(new me.skiincraft.discord.core.configuration.Language(new Locale("pt", "BR")));
	}

	public static Chooser getChooser() {
		return chooser;
	}

	public static Reactions getReaction() {
		return reaction;
	}
}
