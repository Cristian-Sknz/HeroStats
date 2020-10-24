package me.skiincraft.discord.herostats.commands;

import me.skiincraft.api.paladins.common.EndPoint;
import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.player.PlayerChampion;
import me.skiincraft.api.paladins.entity.player.objects.PlayerChampions;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.exceptions.SearchException;
import me.skiincraft.api.paladins.objects.SearchPlayer;
import me.skiincraft.discord.core.command.ContentMessage;
import me.skiincraft.discord.core.configuration.LanguageManager;
import me.skiincraft.discord.core.utils.IntegerUtils;
import me.skiincraft.discord.herostats.HeroStatsBot;
import me.skiincraft.discord.herostats.assets.PaladinsCommand;
import me.skiincraft.discord.herostats.imagebuild.ChampionImage;
import me.skiincraft.discord.herostats.utils.HeroUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChampionCommand extends PaladinsCommand {

	public ChampionCommand() {
		super("champion", Arrays.asList("campeão", "campeao", "champ"), "champion <user> <champion> [platform]");
	}

	public PlayerChampion getChampion(PlayerChampions list, final String name) {
		return list.getAsStream().filter(o -> o.getChampion(Language.Portuguese).get().getName().equalsIgnoreCase(name)).findAny()
				.orElse(null);
	}

	private String[] replaceSpaceChamps(String[] string){
		return String.join(" ", string)
				.toLowerCase()
				.replace("sha lin", "sha_lin")
				.replace("bomb king", "bomb_king")
				.replace("mal damba", "mal'damba")
				.replace("bk", "bomb_king")
				.split(" ");
	}


	@Override
	public void execute(User user, String[] args, TextChannel channel) {
		LanguageManager lang = getLanguageManager();
		if (args.length <= 1) {
			reply("h!"+ getUsage());
			return;
		}

		String[] newArgs = replaceSpaceChamps(args);

		EndPoint requester = HeroStatsBot.getPaladins().getSessions().get(0).getEndPoint();
		Champion champ = requester.getChampions(Language.Portuguese)
				.get()
				.getAsStream()
				.filter(o -> o.getName().equalsIgnoreCase(newArgs[1].replace("_", " ")))
				.findAny()
				.orElse(null);
		
		if (champ == null) {
			reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_INEXISTENT_CHAMPION"), lang.getString("Warnings", "INEXISTENT_CHAMPION")).build());
			return;
		}

		System.out.println(String.join(" ", newArgs));

		try {
			SearchPlayer searchPlayer = (newArgs.length == 3)
					? searchPlayer(newArgs[0], Platform.getPlatformByName(newArgs[2]))
					: searchPlayer((newArgs[0]), Platform.PC);

			if (searchPlayer.isPrivacyFlag()) {
				reply(TypeEmbed.privateProfile().build());
				return;
			}

			PlayerChampions ranks = requester.getPlayerChampions(searchPlayer.getUserId()).get();
			PlayerChampion crank = ranks.getAsStream().filter(o -> o.getChampionId() == champ.getId()).findFirst().orElse(null);
			
			if (crank == null) {
				reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_CHAMPION_NOT_LOCATED"), lang.getString("Warnings", "CHAMPION_NOT_LOCATED")).build());
				return;
			}

			reply(TypeEmbed.processing().build(), processing -> {
				InputStream input = ChampionImage.drawImage(crank);
				reply(new ContentMessage(embed(crank, ranks.getAsList(), searchPlayer).build(), input, "png"));
				processing.delete().queue();
			});
		} catch (SearchException e) {
			reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_INEXISTENT_USER"), lang.getString("Warnings", "INEXISTENT_USER")).build());
		} catch (Exception e){
			reply(TypeEmbed.errorMessage(e, channel).build());
		}
	}

	public EmbedBuilder embed(PlayerChampion rank, List<PlayerChampion> lista, SearchPlayer searchPlayer) {
		EmbedBuilder embed = new EmbedBuilder();
		LanguageManager lang = getLanguageManager();

		int place = lista.indexOf(rank);

		Champion champion = rank.getChampion(Language.Portuguese).get();
		embed.setAuthor(searchPlayer.getInGameName(), null, champion.getIcon());
		embed.setTitle(lang.getString(this.getClass(), "EMBEDTITLE"));
		embed.setThumbnail(champion.getIcon());
		
		String placemessage = (place == 1)
				? lang.getString(this.getClass(), "BESTCHAMPION").replace("{CHAMPION}", rank.getChampionName())
						.replace("{PLAYER}", searchPlayer.getInGameName())
				: lang.getString(this.getClass(), "PLACECHAMPION").replace("{CHAMPION}", rank.getChampionName())
						.replace("{PLACE}", place + "º").replace("{PLAYER}", searchPlayer.getInGameName());

		String playedtime = (TimeUnit.MILLISECONDS.toMinutes(rank.getMillisPlayed()) / 60 != 0) ? TimeUnit.MILLISECONDS.toMinutes(rank.getMillisPlayed()) / 60 + " hora(s)"
				: TimeUnit.MILLISECONDS.toMinutes(rank.getMillisPlayed()) + " minuto(s)";

		embed.setDescription("<:cards:728729369756958750> " + placemessage);
		embed.appendDescription("\n:clock3: " + lang.getString(this.getClass(), "TIMEPLAYED") + playedtime);
		embed.setColor(HeroUtils.paladinsClassColor(champion));

		embed.setFooter("Jogou pela ultima vez em");
		embed.setTimestamp(rank.getLastPlayed());
		DecimalFormat df = new DecimalFormat("#.0");

		embed.addField("Modo", "Todos os modos.", true);
		embed.addField("Taxa de Vitoria", IntegerUtils.getPorcentagem(rank.getWins() + rank.getLosses(), rank.getWins()), true);
		embed.addField("Taxa de Abates", IntegerUtils.getPorcentagem(rank.getKills() + rank.getDeaths(), rank.getKills()), true);

		return embed;
	}

}
