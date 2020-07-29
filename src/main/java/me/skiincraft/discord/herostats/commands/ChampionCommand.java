package me.skiincraft.discord.herostats.commands;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.ocpsoft.prettytime.PrettyTime;

import me.skiincraft.api.paladins.Queue;
import me.skiincraft.api.paladins.common.Champion;
import me.skiincraft.api.paladins.common.ChampionRank;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.exceptions.PlayerNotFoundException;
import me.skiincraft.api.paladins.objects.SearchPlayer;
import me.skiincraft.discord.core.commands.Command;
import me.skiincraft.discord.core.entity.BotTextChannel;
import me.skiincraft.discord.core.entity.BotUser;
import me.skiincraft.discord.core.entity.ContentMessage;
import me.skiincraft.discord.core.multilanguage.Language;
import me.skiincraft.discord.core.multilanguage.LanguageManager;
import me.skiincraft.discord.core.utils.StringUtils;
import me.skiincraft.discord.herostats.HeroStatsBot;
import me.skiincraft.discord.herostats.imagebuild.ChampionImage;
import net.dv8tion.jda.api.EmbedBuilder;

public class ChampionCommand extends Command {

	public ChampionCommand() {
		super("champion", Arrays.asList("campeão", "campeao", "champ"), "champion <user> <champion> [platform]");
	}

	public String existsChampion(List<String> list, final String name) {
		if (name.toLowerCase() == "bk") {
			return "Bomb King";
		}
		if (name.toLowerCase() == "bombking") {
			return "Bomb King";
		}
		if (name.toLowerCase() == "shalin") {
			return "Sha lin";
		}
		return list.stream().filter(o -> o.equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	public ChampionRank getChampion(List<ChampionRank> list, final String name) {
		return list.stream().filter(o -> o.getChampion().getChampionName().equalsIgnoreCase(name)).findAny()
				.orElse(null);
	}

	@Override
	public void execute(BotUser user, String[] args, BotTextChannel channel) {
		LanguageManager lang = getLanguageManager();
		if (args.length <= 1) {
			// TODO Mensagem de advertencia.
			reply("h!"+ getUsage());
			return;
		}
		Queue requester = HeroStatsBot.getPaladins().getSessionsCache().get(0).getRequester();
		//List<String> championsname = requester.getLoadedchampions().stream().map(f -> f.getChampionName()).collect(Collectors.toList());

		Champion champ = requester.getLoadedchampions().stream().filter(o -> StringUtils.containsEqualsIgnoreCase(o.getChampionName(), args[1])).findAny().orElse(null);
		if (champ == null) {
			reply(TypeEmbed.simpleEmbed("^.^", lang.getString("Warnings", "INEXISTENT_CHAMPION")).build());
			System.out.println(requester.getLoadedchampions().stream().filter(o -> o.getChampionName().contains("Bomb ")).findAny().orElse(null));
			return;
		}

		try {
			List<SearchPlayer> searchlist = (args.length == 3)
					? requester.searchPlayer(args[0], Platform.getPlatformByName(args[2]))
					: requester.searchPlayer(args[0]);

			List<ChampionRank> ranks = requester.getChampionRanks(searchlist.get(0).getUserId());
			ChampionRank crank = ranks.stream().filter(o -> o.getChampionId() == champ.getChampionId()).findFirst().orElse(null);
			
			if (crank == null) {
				reply(TypeEmbed.simpleEmbed("^.^", lang.getString("Warnings", "CHAMPION_NOT_LOCATED")).build());
				return;
			}
			
			InputStream input = ChampionImage.drawImage(crank);
			reply(new ContentMessage(embed(crank, ranks, searchlist.get(0)).build(), input, "png"));
		} catch (PlayerNotFoundException e) {
			reply(TypeEmbed.simpleEmbed("^.^", lang.getString("Warnings", "INEXISTENT_USER")).build());
			e.printStackTrace();
		}
	}

	public EmbedBuilder embed(ChampionRank rank, List<ChampionRank> lista, SearchPlayer searchPlayer) {
		EmbedBuilder embed = new EmbedBuilder();
		// <:championemote:727241756281929729>
		LanguageManager lang = getLanguageManager();
		int place = 1;
		for (ChampionRank rank2 : lista) {
			if (rank2 == rank) {
				break;
			}
			place++;
		}
		embed.setAuthor(searchPlayer.getInGameName(), null, rank.getChampion().getChampionIcon());
		embed.setTitle(lang.getString(this.getClass(), "EMBEDTITLE"));
		PrettyTime pretty = new PrettyTime(
				(getLanguageManager().getLanguage() == Language.Portuguese) ? new Locale("pt", "BR")
						: new Locale("en", "US"));
		// rank.getLastplayed(),
		embed.setThumbnail(rank.getChampion().getChampionIcon());
		String placemessage = (place == 1)
				? lang.getString(this.getClass(), "BESTCHAMPION").replace("{CHAMPION}", rank.getChampionName())
						.replace("{PLAYER}", searchPlayer.getInGameName())
				: lang.getString(this.getClass(), "PLACECHAMPION").replace("{CHAMPION}", rank.getChampionName())
						.replace("{PLACE}", place + "º").replace("{PLAYER}", searchPlayer.getInGameName());

		embed.setDescription("<:cards:728729369756958750> " + placemessage);
		String playedtime = (rank.getMinutes() / 60 != 0) ? rank.getMinutes() / 60 + " hora(s)"
				: rank.getMinutes() + " minuto(s)";
		embed.appendDescription("\n:clock3: " + lang.getString(this.getClass(), "TIMEPLAYED") + playedtime);
		embed.appendDescription(
				"\n:construction:" + lang.getString(this.getClass(), "LASTPLAY") + pretty.format(rank.getLastplayed()));
		embed.setImage("attachment://" + "champion_command" + ".png");
		return embed;
	}

}
