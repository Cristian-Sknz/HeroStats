package me.skiincraft.discord.herostats.commands;

import me.skiincraft.api.paladins.common.EndPoint;
import me.skiincraft.api.paladins.entity.player.PlayerChampion;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.exceptions.PlayerException;
import me.skiincraft.api.paladins.exceptions.SearchException;
import me.skiincraft.api.paladins.objects.SearchPlayer;
import me.skiincraft.discord.core.command.InteractChannel;
import me.skiincraft.discord.core.configuration.LanguageManager;
import me.skiincraft.discord.herostats.HeroStatsBot;
import me.skiincraft.discord.herostats.assets.Category;
import me.skiincraft.discord.herostats.assets.PaladinsCommand;
import me.skiincraft.discord.herostats.enums.PaladinsClass;
import me.skiincraft.discord.herostats.utils.ImageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ClassStatsCommand extends PaladinsCommand {

	public ClassStatsCommand() {
		super("class", Arrays.asList("classtatus", "classstats", "classstatus", "classe"),
				"class <player> <class> [platform]");
	}

	public Category category() {
		return Category.Statistics;
	}

	@Override
	public void execute(Member user, String[] args, InteractChannel channel) {
		LanguageManager lang = getLanguageManager(channel.getTextChannel().getGuild());
		if (args.length <= 1) {
			channel.reply("h!" + getUsage());
			return;
		}

		if (convertClassString(args[1]) == null) {
			channel.reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_INVALID_CHAMPION_CLASS"), lang.getString("Warnings", "INVALID_CHAMPION_CLASS")).build());
			return;
		}

		EndPoint requester = HeroStatsBot.getPaladins().getSessions().get(0).getEndPoint();
		try {
			SearchPlayer searchPlayer = (args.length >= 3)
					? searchPlayer(args[0], Platform.getPlatformByName(args[2]))
					: searchPlayer(args[0], Platform.PC);

			if (searchPlayer.isPrivacyFlag()) {
				channel.reply(TypeEmbed.privateProfile().build());
				return;
			}

			channel.reply(TypeEmbed.processing().appendDescription("\nEssa operação usa muito processamento...").build(), message ->{
				List<PlayerChampion> ranks = requester.getPlayerChampions(searchPlayer.getUserId()).get().getAsList();
				List<PlayerChampion> classrank = getClassChampionRanks(ranks, convertClassString(args[1]));

				channel.reply(embed(searchPlayer, classrank, PaladinsClass.Support).build());
				message.delete().queue();
			});
		} catch (SearchException e) {
			channel.reply(TypeEmbed.simpleEmbed(getLanguageManager(channel.getTextChannel().getGuild()).getString("Warnings", "T_INEXISTENT_USER"), getLanguageManager(channel.getTextChannel().getGuild()).getString("Warnings", "INEXISTENT_USER")).build());
		} catch (PlayerException e) {
			channel.reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_INEXISTENT_USER"), lang.getString("Warnings", "INEXISTENT_USER")).build());
		} catch (Exception e){
			channel.reply(TypeEmbed.errorMessage(e, channel.getTextChannel()).build());
		}
	}

	public EmbedBuilder embed(SearchPlayer search, List<PlayerChampion> ranks, PaladinsClass clazz) {
		EmbedBuilder embed = new EmbedBuilder();
		String url = getClassImage(ranks.get(0).getChampion(Language.Portuguese).get().getRole().replace("Paladins ", ""));
		embed.setAuthor(search.getName(), null, url);
		embed.setThumbnail(url);
		Map<String, Integer> calcs = calcRanks(ranks);
		embed.setDescription(":mag_right: Partidas Jogadas: " + calcs.get("matchs"));
		embed.appendDescription("\n:game_die: Vitorias/Derrotas: " + calcs.get("wins") + "/" + calcs.get("losses"));

		StringBuilder moreplayed = new StringBuilder();
		ranks.sort((PlayerChampion o1, PlayerChampion o2) -> Long.compare(o2.getMillisPlayed(), o1.getMillisPlayed()));
		for (int i = 0; i < ranks.size(); i++) {
			PlayerChampion champrank = ranks.get(i);
			moreplayed.append("<:championemote:727241756281929729> ").append(champrank.getChampionName()).append(" - ").append(TimeUnit.MILLISECONDS.toMinutes(champrank.getMillisPlayed()) / 60).append(" Hora(s)\n");
			if (i == 3) {
				break;
			}
		}
		StringBuilder morelevel = new StringBuilder();
		ranks.sort((PlayerChampion o1, PlayerChampion o2) -> Integer.compare(o2.getChampionLevel(), o1.getChampionLevel()));
		for (int i = 0; i < ranks.size(); i++) {
			PlayerChampion champrank = ranks.get(i);
			morelevel.append("<:championemote:727241756281929729> ").append(champrank.getChampionName()).append(" - ").append(champrank.getChampionLevel()).append(" Level\n");
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

	private Map<String, Integer> calcRanks(List<PlayerChampion> ranks) {
		Map<String, Integer> calc = new HashMap<>();
		int wins = 0;
		int losses = 0;
		int kills = 0;
		int deaths = 0;
		int assists = 0;

		for (PlayerChampion rank : ranks) {
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

	public static List<PlayerChampion> getClassChampionRanks(List<PlayerChampion> allchamps, String clazz) {
		List<PlayerChampion> list = new ArrayList<>();
		for (PlayerChampion rank : allchamps) {
			String r = rank.getChampion(Language.Portuguese).get().getRole().replace("Paladins ", "");
			if (r.equalsIgnoreCase(clazz)) {
				list.add(rank);
			}
		}

		list.sort(Comparator.comparingInt(PlayerChampion::getChampionLevel));
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
