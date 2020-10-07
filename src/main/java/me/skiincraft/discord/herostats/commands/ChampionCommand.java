package me.skiincraft.discord.herostats.commands;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.ocpsoft.prettytime.PrettyTime;

import me.skiincraft.api.paladins.common.EndPoint;
import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.player.PlayerChampion;
import me.skiincraft.api.paladins.entity.player.objects.PlayerChampions;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.objects.SearchPlayer;
import me.skiincraft.discord.core.command.Command;
import me.skiincraft.discord.core.command.ContentMessage;
import me.skiincraft.discord.core.configuration.LanguageManager;
import me.skiincraft.discord.core.utils.StringUtils;
import me.skiincraft.discord.herostats.HeroStatsBot;
import me.skiincraft.discord.herostats.imagebuild.ChampionImage;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

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

	public PlayerChampion getChampion(PlayerChampions list, final String name) {
		return list.getAsStream().filter(o -> o.getChampion(Language.Portuguese).get().getName().equalsIgnoreCase(name)).findAny()
				.orElse(null);
	}

	@Override
	public void execute(User user, String[] args, TextChannel channel) {
		LanguageManager lang = getLanguageManager();
		if (args.length <= 1) {
			reply("h!"+ getUsage());
			return;
		}
		EndPoint requester = HeroStatsBot.getPaladins().getSessions().get(0).getEndPoint();
		Champion champ = requester.getChampions(Language.Portuguese).get().getAsStream().filter(o -> StringUtils.containsEqualsIgnoreCase(o.getName(), args[1])).findAny().orElse(null);
		
		if (champ == null) {
			reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_INEXISTENT_CHAMPION"), lang.getString("Warnings", "INEXISTENT_CHAMPION")).build());
			return;
		}

		try {
			List<SearchPlayer> searchlist = (args.length == 3)
					? requester.searchPlayer(args[0], Platform.getPlatformByName(args[2])).get().getAsList()
					: requester.searchPlayer((args[0]), Platform.PC).get().getAsList();

			PlayerChampions ranks = requester.getPlayerChampions(searchlist.get(0).getUserId()).get();
			PlayerChampion crank = ranks.getAsStream().filter(o -> o.getChampionId() == champ.getId()).findFirst().orElse(null);
			
			if (crank == null) {
				reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_CHAMPION_NOT_LOCATED"), lang.getString("Warnings", "CHAMPION_NOT_LOCATED")).build());
				return;
			}
			
			
			InputStream input = ChampionImage.drawImage(crank);
			reply(new ContentMessage(embed(crank, ranks.getAsList(), searchlist.get(0)).build(), input, "png"));
		} catch (Exception e) {
			reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_INEXISTENT_USER"), lang.getString("Warnings", "INEXISTENT_USER")).build());
			e.printStackTrace();
		}
	}

	public EmbedBuilder embed(PlayerChampion rank, List<PlayerChampion> lista, SearchPlayer searchPlayer) {
		EmbedBuilder embed = new EmbedBuilder();
		// <:championemote:727241756281929729>
		LanguageManager lang = getLanguageManager();
		int place = 1;
		for (PlayerChampion rank2 : lista) {
			if (rank2 == rank) {
				break;
			}
			place++;
		}
		PrettyTime pretty = new PrettyTime(getLanguageManager().getLanguage().getLocale());
		Champion champion = rank.getChampion(Language.Portuguese).get();
		embed.setAuthor(searchPlayer.getInGameName(), null, champion.getIcon());
		embed.setTitle(lang.getString(this.getClass(), "EMBEDTITLE"));
		embed.setThumbnail(champion.getIcon());
		
		String placemessage = (place == 1)
				? lang.getString(this.getClass(), "BESTCHAMPION").replace("{CHAMPION}", rank.getChampionName())
						.replace("{PLAYER}", searchPlayer.getInGameName())
				: lang.getString(this.getClass(), "PLACECHAMPION").replace("{CHAMPION}", rank.getChampionName())
						.replace("{PLACE}", place + "º").replace("{PLAYER}", searchPlayer.getInGameName());

		embed.setDescription("<:cards:728729369756958750> " + placemessage);
		String playedtime = (TimeUnit.MILLISECONDS.toMinutes(rank.getPlayedTime()) / 60 != 0) ? TimeUnit.MILLISECONDS.toMinutes(rank.getPlayedTime()) / 60 + " hora(s)"
				: TimeUnit.MILLISECONDS.toMinutes(rank.getPlayedTime()) + " minuto(s)";
		embed.appendDescription("\n:clock3: " + lang.getString(this.getClass(), "TIMEPLAYED") + playedtime);
		embed.setFooter(":)", champion.getIcon());
		try {
			embed.appendDescription(
					"\n:construction:" + lang.getString(this.getClass(), "LASTPLAY") + pretty.format(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a").parse(rank.getLastPlayed())));
		} catch (ParseException e) {
			e.printStackTrace();
			return embed;
		}
		return embed;
	}

}
