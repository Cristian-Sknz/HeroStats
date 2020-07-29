package me.skiincraft.discord.herostats.commands;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import me.skiincraft.api.paladins.Queue;
import me.skiincraft.api.paladins.common.ChampionRank;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.exceptions.PlayerNotFoundException;
import me.skiincraft.api.paladins.objects.SearchPlayer;
import me.skiincraft.discord.core.commands.Command;
import me.skiincraft.discord.core.entity.BotTextChannel;
import me.skiincraft.discord.core.entity.BotUser;
import me.skiincraft.discord.core.multilanguage.LanguageManager;
import me.skiincraft.discord.core.utils.ImageUtils;
import me.skiincraft.discord.herostats.HeroStatsBot;
import me.skiincraft.discord.herostats.enums.PaladinsClass;
import net.dv8tion.jda.api.EmbedBuilder;

public class ClassStatsCommand extends Command {

	public ClassStatsCommand() {
		super("class", Arrays.asList("classtatus", "classstats", "classstatus", "classe"),
				"class <player> <class> [platform]");
	}

	@Override
	public void execute(BotUser user, String[] args, BotTextChannel channel) {
		LanguageManager lang = getLanguageManager();
		if (args.length <= 1) {
			// TODO Mensagem de advertencia.
			reply("h!" + getUsage());
			return;
		}

		if (convertClassString(args[1]) == null) {
			reply(TypeEmbed.simpleEmbed("^.^", lang.getString("Warnings", "INVALID_CHAMPION_CLASS")).build());
			return;
		}

		Queue requester = HeroStatsBot.getPaladins().getSessionsCache().get(0).getRequester();
		try {
			List<SearchPlayer> searchs = (args.length >= 3)
					? requester.searchPlayer(args[0], Platform.getPlatformByName(args[2]))
					: requester.searchPlayer(args[0]);

			List<ChampionRank> ranks = requester.getChampionRanks(searchs.get(0).getUserId());
			List<ChampionRank> classrank = getClassChampionRanks(ranks, convertClassString(args[1]));

			reply(embed(searchs.get(0), classrank, PaladinsClass.Support).build());

		} catch (PlayerNotFoundException e) {
			reply(TypeEmbed.simpleEmbed("^.^", lang.getString("Warnings", "INEXISTENT_USER")).build());
		}
	}

	public EmbedBuilder embed(SearchPlayer search, List<ChampionRank> ranks, PaladinsClass clazz) {
		EmbedBuilder embed = new EmbedBuilder();
		String url = getClassImage(ranks.get(0).getChampion().getRole().replace("Paladins ", ""));
		embed.setAuthor(search.getName(), null, url);
		embed.setThumbnail(url);
		Map<String, Integer> calcs = calcRanks(ranks);
		embed.setDescription(":mag_right: Partidas Jogadas: " + calcs.get("matchs"));
		embed.appendDescription("\n:game_die: Vitorias/Derrotas: " + calcs.get("wins") + "/" + calcs.get("losses"));

		List<ChampionRank> sortedlist = ranks;
		StringBuffer moreplayed = new StringBuffer();
		sortedlist.sort((ChampionRank o1, ChampionRank o2) -> {
			return Integer.compare(o2.getMinutes(), o1.getMinutes());
		});
		for (int i = 0; i < sortedlist.size(); i++) {
			ChampionRank champrank = sortedlist.get(i);
			moreplayed.append("<:championemote:727241756281929729> " + champrank.getChampionName() + " - "
					+ champrank.getMinutes() / 60 + " Hora(s)\n");
			if (i == 3) {
				break;
			}
		}
		StringBuffer morelevel = new StringBuffer();
		sortedlist.sort((ChampionRank o1, ChampionRank o2) -> {
			return Integer.compare(o2.getChampionLevel(), o1.getChampionLevel());
		});
		for (int i = 0; i < sortedlist.size(); i++) {
			ChampionRank champrank = sortedlist.get(i);
			morelevel.append("<:championemote:727241756281929729> " + champrank.getChampionName() + " - "
					+ champrank.getChampionLevel() + " Level\n");
			if (i == 3) {
				break;
			}
		}
		embed.addField("Mais Jogados: ", moreplayed.toString(), true);
		embed.addField("Maiores Leveis: ", morelevel.toString(), true);
		embed.addField("KDA", calcs.get("kills") + "/" + calcs.get("deaths") + "/" + calcs.get("assists"), false);

		try {
			embed.setColor(ImageUtils.getPredominatColor(ImageIO.read(new URL(url))));
		} catch (IOException e) {
			embed.setColor(Color.CYAN);
		}

		return embed;
	}

	private Map<String, Integer> calcRanks(List<ChampionRank> ranks) {
		Map<String, Integer> calc = new HashMap<>();
		int wins = 0;
		int losses = 0;
		int kills = 0;
		int deaths = 0;
		int assists = 0;

		for (ChampionRank rank : ranks) {
			wins += rank.getWins();
			losses += rank.getLosses();
			kills += rank.getKills();
			deaths += rank.getDeaths();
			assists += rank.getAssists();
		}

		calc.put("matchs", wins + losses);
		calc.put("wins", wins);
		calc.put("losses", losses);
		calc.put("kills", kills);
		calc.put("deaths", deaths);
		calc.put("assists", assists);

		return calc;
	}

	public static List<ChampionRank> getClassChampionRanks(List<ChampionRank> allchamps, String clazz) {
		List<ChampionRank> list = new ArrayList<>();
		for (ChampionRank rank : allchamps) {
			String r = rank.getChampion().getRole().replace("Paladins ", "");
			if (r.equalsIgnoreCase(clazz)) {
				list.add(rank);
			}
		}

		list.sort((ChampionRank o1, ChampionRank o2) -> {
			return Integer.compare(o1.getChampionLevel(), o2.getChampionLevel());
		});

		return list;
	}

	public String convertClassString(String string) {
		if (string.equalsIgnoreCase("Dano")) {
			return "Dano";
		}
		if (string.equalsIgnoreCase("Flanco")) {
			return "Flanco";
		}
		if (string.equalsIgnoreCase("Suporte")) {
			return "Suporte";
		}
		if (string.equalsIgnoreCase("Tanque")) {
			return "Tanque";
		}
		if (string.equalsIgnoreCase("Damage")) {
			return "Dano";
		}
		if (string.equalsIgnoreCase("Flank")) {
			return "Flanco";
		}
		if (string.equalsIgnoreCase("Support")) {
			return "Suporte";
		}
		if (string.equalsIgnoreCase("Frontline")) {
			return "Tanque";
		}
		if (string.equalsIgnoreCase("Front")) {
			return "Tanque";
		}
		if (string.equalsIgnoreCase("Tank")) {
			return "Tanque";
		}
		return null;
	}

	public String getClassImage(String clazz) {
		if (clazz.equalsIgnoreCase("Dano")) {
			return "https://i.imgur.com/Mot5MkL.png";
		}
		if (clazz.equalsIgnoreCase("Flanco")) {
			return "https://i.imgur.com/s3pIxKh.png";
		}
		if (clazz.equalsIgnoreCase("Tanque")) {
			return "https://i.imgur.com/7vvaMxu.png";
		}
		if (clazz.equalsIgnoreCase("Suporte")) {
			return "https://i.imgur.com/y6fUd7z.png";
		}

		return "https://i.imgur.com/Mot5MkL.png";
	}

}
