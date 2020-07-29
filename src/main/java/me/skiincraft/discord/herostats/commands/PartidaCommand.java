package me.skiincraft.discord.herostats.commands;

import java.awt.Color;
import java.io.InputStream;
import java.util.Arrays;

import me.skiincraft.api.paladins.Paladins;
import me.skiincraft.api.paladins.entity.LiveMatch;
import me.skiincraft.api.paladins.entity.Session;
import me.skiincraft.api.paladins.enums.PaladinsQueue;
import me.skiincraft.api.paladins.exceptions.NoMatchQueueException;
import me.skiincraft.api.paladins.exceptions.OfflinePlayerException;
import me.skiincraft.api.paladins.exceptions.PlayerNotFoundException;
import me.skiincraft.api.paladins.matches.LivePlayer;
import me.skiincraft.discord.core.commands.Command;
import me.skiincraft.discord.core.entity.BotTextChannel;
import me.skiincraft.discord.core.entity.BotUser;
import me.skiincraft.discord.core.entity.ContentMessage;
import me.skiincraft.discord.core.multilanguage.LanguageManager;
import me.skiincraft.discord.core.utils.StringUtils;
import me.skiincraft.discord.herostats.HeroStatsBot;
import me.skiincraft.discord.herostats.imagebuild.PartidaImage;
import net.dv8tion.jda.api.EmbedBuilder;

public class PartidaCommand extends Command {

	public PartidaCommand() {
		super("livematch", Arrays.asList("partida"), "<username>");
	}

	private Session session;

	@Override
	public void execute(BotUser user, String[] args, BotTextChannel channel) {
		Paladins paladins = HeroStatsBot.getPaladins();
		session = paladins.getSessionsCache().get(0);
		if (args.length == 0) {
			return;
		}
		try {
			LiveMatch match = session.getRequester().getLiveMatchDetails(args[0]);
			InputStream input = PartidaImage.drawImage(StringUtils.arrayToString2(1, match.getMapGame().split(" ")),
					match);
			reply(new ContentMessage(embed(match, args[0]).build(), input, "png"));
		} catch (NoMatchQueueException e) {
			reply(TypeEmbed.simpleEmbed("^.^","Este jogador não esta em nenhuma partida.").build());
		} catch (PlayerNotFoundException e) {
			reply(TypeEmbed.simpleEmbed("^.^","Este jogador solicitado não existe.").build());
		} catch (OfflinePlayerException e) {
			reply(TypeEmbed.simpleEmbed("^.^","Este jogador solicitado não esta online.").build());
		}
	}

	public EmbedBuilder embed(LiveMatch match, String player) {
		EmbedBuilder embed = new EmbedBuilder();
		LanguageManager lang = getLanguageManager();
		LivePlayer principal = null;
		for (LivePlayer live : match.getPlayers()) {
			if (live.getPlayerName().toLowerCase().contains(player.toLowerCase())) {
				principal = live;
				break;
			}
		}

		embed.setThumbnail(principal.getChampion().getChampionIcon());
		embed.setAuthor(principal.getPlayerName(), null, principal.getChampion().getChampionIcon());
		embed.setTitle("Partida Atual");

		String search = StringUtils.arrayToString2(1, match.getMapGame().split(" ")).replace("'", "_").replace(" ", "_")
				.toUpperCase();

		String mapname = lang.getString("Mapas", search);
		embed.setDescription(":map:" + lang.getString("PartidaCommand", "MATCHMAP") + " " + mapname + "\n");
		embed.appendDescription(
				":video_game:" + lang.getString("PartidaCommand", "GAMEMODE") + convertQueue(match.getQueue()) + "\n");
		embed.appendDescription(
				":id:" + lang.getString("PartidaCommand", "MATCHID") + "`" + match.getMatchId() + "`\n");
		embed.setImage("attachment://" + "partida" + ".png");

		embed.setColor(roleColor(principal.getChampion().getRole()));
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

	public String convertQueue(PaladinsQueue queue) {
		LanguageManager lang = getLanguageManager();
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
