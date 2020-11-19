package me.skiincraft.discord.herostats.commands;

import me.skiincraft.api.paladins.Paladins;
import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.match.LiveMatch;
import me.skiincraft.api.paladins.entity.match.LivePlayer;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.enums.PlayerStatus;
import me.skiincraft.api.paladins.enums.Queue;
import me.skiincraft.api.paladins.exceptions.MatchException;
import me.skiincraft.api.paladins.exceptions.PlayerException;
import me.skiincraft.api.paladins.exceptions.SearchException;
import me.skiincraft.api.paladins.objects.SearchPlayer;
import me.skiincraft.discord.core.command.ContentMessage;
import me.skiincraft.discord.core.command.InteractChannel;
import me.skiincraft.discord.core.configuration.LanguageManager;
import me.skiincraft.discord.core.utils.StringUtils;
import me.skiincraft.discord.herostats.HeroStatsBot;
import me.skiincraft.discord.herostats.assets.Category;
import me.skiincraft.discord.herostats.imagebuild.LiveMatchImage;
import me.skiincraft.discord.herostats.assets.PaladinsCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.io.InputStream;
import java.util.*;
import java.util.List;

public class PartidaCommand extends PaladinsCommand {

	public PartidaCommand() {
		super("livematch", Collections.singletonList("partida"), "partida <username>");
	}

	public Category category() {
		return Category.Match;
	}

	@Override
	public void execute(Member user, String[] args, InteractChannel channel) {
		Paladins paladins = HeroStatsBot.getPaladins();
		LanguageManager lang = getLanguageManager(channel.getTextChannel().getGuild());
		if (args.length == 0) {
			channel.reply("h!" + getUsage());
			return;
		}

		try {
			SearchPlayer searchPlayer = (args.length >= 2)
					? searchPlayer(args[0], Platform.getPlatformByName(args[1]))
					: searchPlayer(args[0], Platform.PC);

			if (searchPlayer.isPrivacyFlag()) {
				channel.reply(TypeEmbed.privateProfile().build());
				return;
			}

			PlayerStatus status = playerStatus(searchPlayer.getUserId());
			if (status.getStatus() == PlayerStatus.Status.Online){
				channel.reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_OFFLINE_USER"), lang.getString("Warnings", "OFFLINE_USER")).build());
				return;
			}

			if (status.isInMatch()){
				channel.reply(TypeEmbed.processing().build(), message -> {
					try {
						LiveMatch live = liveMatch(status.getMatchId());
						String map = (live.getMapName() != null) ? StringUtils.arrayToString2(1, live.getMapName().split(" ")) : null;
						InputStream input = LiveMatchImage.drawImagePartida(map, live);
						channel.reply(new ContentMessage(embed(live, args[0], getLanguageManager(channel.getTextChannel().getGuild())).build(), input, "png"));
						message.delete().queue();
					} catch (Exception e){
						channel.reply("Ops... Tive um problema: `" + e.getMessage() + "`");
						e.printStackTrace();
					}
				});
			} else {
				channel.reply(TypeEmbed.simpleEmbed(lang.getString("Warnings","T_NO_MATCH_FOUND"),lang.getString("Warnings","NO_MATCH_FOUND")).build());
			}
		} catch (MatchException e) {
			channel.reply(TypeEmbed.simpleEmbed(lang.getString("Warnings","T_NO_MATCH_FOUND"),lang.getString("Warnings","NO_MATCH_FOUND")).build());
		} catch (PlayerException | SearchException e) {
			channel.reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_INEXISTENT_USER"), lang.getString("Warnings", "INEXISTENT_USER")).build());
		} catch (Exception e){
			channel.reply(TypeEmbed.errorMessage(e, channel.getTextChannel()).build());
		}
	}

	public EmbedBuilder embed(LiveMatch match, String player, LanguageManager lang) {
		EmbedBuilder embed = new EmbedBuilder();
		LivePlayer principal = null;
		List<LivePlayer> all = new ArrayList<>();
		all.addAll(match.getTeamBlue());
		all.addAll(match.getTeamRed());

		for (LivePlayer live : all) {
			if (live.getPlayerName().toLowerCase().contains(player.toLowerCase())) {
				principal = live;
				break;
			}
		}

		Champion champion = Objects.requireNonNull(principal).getChampion(Language.Portuguese).get();

		embed.setThumbnail(champion.getIcon());
		embed.setAuthor(principal.getPlayerName(), null, champion.getIcon());
		embed.setTitle("Partida Andamento");

		String search = (match.getMapName() == null) ? "Indisponivel" : StringUtils.arrayToString2(1, match.getMapName().split(" ")).replace("'", "_").replace(" ", "_").toUpperCase();

		String mapname = (match.getMapName() == null)? search : lang.getString("Mapas", search);
		embed.setDescription(":map:" + lang.getString("PartidaCommand", "MATCHMAP") + " " + mapname + "\n");
		embed.appendDescription(":id: " + lang.getString("PartidaCommand", "MATCHID") + "`" + match.getMatchId() + "`\n");
		embed.appendDescription(":game_die: Modo de Jogo: " + lang.getString("PaladinsQueue", match.getQueue().name()));
		embed.setImage("attachment://" + "partida" + ".png");

		embed.setColor(roleColor(principal.getChampion(Language.Portuguese).get().getRole()));
		return embed;
	}

	public Color roleColor(String role) {
		if (role.contains("Dano") || role.contains("Damage")) {
			return new Color(255, 64, 64);
		}
		if (role.contains("Flanco") || role.contains("Flank")) {
			return new Color(255, 255, 64);
		}
		if (role.contains("Tanque") || role.contains("Frontline")) {
			return new Color(36, 168, 255);
		}
		if (role.contains("Suporte") || role.contains("Support")) {
			return new Color(0, 200, 20);
		}

		return null;
	}

	public String convertQueue(Queue queue, LanguageManager lang) {
		if (queue.name().contains("Live_Competitive")) {
			return lang.getString("PaladinsQueue", "LIVE_COMPETITIVE");
		}
		if (queue.name().contains("Live_Test_Maps")) {
			return lang.getString("PaladinsQueue", "LIVE_TEST_MAPS");
		}
		if (queue.name().contains("Live_Onslaught")) {
			return lang.getString("PaladinsQueue", "LIVE_ONSLAUGHT");
		}
		if (queue.name().contains("Live_Siege")) {
			return lang.getString("PaladinsQueue", "LIVE_SIEGE");
		}
		if (queue.name().contains("Live_Team_DeathMatch")) {
			return lang.getString("PaladinsQueue", "LIVE_TEAM_DEATHMATCH");
		}
		if (queue.name().contains("Live_Pratice_Siege")) {
			return lang.getString("PaladinsQueue", "LIVE_PRATICE_TEAM_DEATHMATCH");
		}
		if (queue.name().contains("Live_Pratice_Team_Deathmatch")) {
			return lang.getString("PaladinsQueue", "LIVE_PRATICE_TEAM_DEATHMATCH");
		}
		if (queue.name().contains("Live_Pratice_Onslaught")) {
			return lang.getString("PaladinsQueue", "LIVE_PRATICE_ONSLAUGHT");
		}
		if (queue.name().contains("Custom_Onslaught")) {
			return lang.getString("PaladinsQueue", "CUSTOM_ONSLAUGHT");
		}
		if (queue.name().contains("Custom_Siege")) {
			return lang.getString("PaladinsQueue", "CUSTOM_SIEGE");
		}
		if (queue.name().contains("Custom_Team_Deathmatch")) {
			return lang.getString("PaladinsQueue", "CUSTOM_TEAM_DEATHMATCH");
		}

		return queue.name();
	}

}
