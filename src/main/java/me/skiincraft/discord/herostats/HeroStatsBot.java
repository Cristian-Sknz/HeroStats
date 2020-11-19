package me.skiincraft.discord.herostats;

import me.skiincraft.api.paladins.Paladins;
import me.skiincraft.api.paladins.PaladinsBuilder;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.common.PresenceUpdater;
import me.skiincraft.discord.core.common.chooser.Chooser;
import me.skiincraft.discord.core.common.chooser.ChooserListeners;
import me.skiincraft.discord.core.common.reactions.ReactionListeners;
import me.skiincraft.discord.core.common.reactions.Reactions;
import me.skiincraft.discord.core.plugin.OusuPlugin;
import me.skiincraft.discord.herostats.commands.*;
import me.skiincraft.discord.herostats.session.SessionConfiguration;
import net.dv8tion.jda.api.entities.Activity;

import java.io.IOException;
import java.util.Arrays;
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
		OusuCore.registerCommand(new StatusCommand());
		OusuCore.registerCommand(new ChampionCommand());
		OusuCore.registerCommand(new DeckCommand());
		OusuCore.registerCommand(new ClassStatsCommand());
		OusuCore.registerCommand(new SplitCommand());
		OusuCore.registerCommand(new HelpCommand());
		OusuCore.registerCommand(new PartidaCommand());
        OusuCore.registerCommand(new LeaderboardCommand());
		OusuCore.registerCommand(new PingCommand());
		OusuCore.registerCommand(new DataUsedCommand());
		OusuCore.registerCommand(new HistoryCommand());

		ChooserListeners listener = new ChooserListeners();
		chooser = Chooser.of(listener);
		OusuCore.registerListener(listener);

		ReactionListeners rlistener = new ReactionListeners();
		reaction = Reactions.of(rlistener);
		OusuCore.registerListener(rlistener);

		OusuCore.addLanguage(new me.skiincraft.discord.core.configuration.Language(new Locale("pt", "BR")));
		new PresenceUpdater(Arrays.asList(Activity.listening("h!help para ajuda!"), Activity.listening("Problemas? Reporte para o developer!")));
	}

	public static Chooser getChooser() {
		return chooser;
	}

	public static Reactions getReaction() {
		return reaction;
	}
}
