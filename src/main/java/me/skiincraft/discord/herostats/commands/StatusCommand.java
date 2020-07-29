package me.skiincraft.discord.herostats.commands;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import me.skiincraft.api.paladins.common.ChampionRank;
import me.skiincraft.api.paladins.entity.PaladinsPlayer;
import me.skiincraft.api.paladins.entity.Session;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.enums.PlayerStatus;
import me.skiincraft.api.paladins.enums.PlayerStatus.Status;
import me.skiincraft.api.paladins.exceptions.PlayerNotFoundException;
import me.skiincraft.api.paladins.objects.SearchPlayer;
import me.skiincraft.discord.core.apis.Emoji;
import me.skiincraft.discord.core.commands.Command;
import me.skiincraft.discord.core.entity.BotTextChannel;
import me.skiincraft.discord.core.entity.BotUser;
import me.skiincraft.discord.core.entity.ContentMessage;
import me.skiincraft.discord.core.multilanguage.LanguageManager;
import me.skiincraft.discord.core.utils.ImageUtils;
import me.skiincraft.discord.core.utils.IntegerUtils;
import me.skiincraft.discord.herostats.HeroStatsBot;
import me.skiincraft.discord.herostats.imagebuild.StatusImage;
import net.dv8tion.jda.api.EmbedBuilder;

public class StatusCommand extends Command {

	public StatusCommand() {
		super("status", Arrays.asList("stats"), "status <nickname> [platform]");
	}

	@Override
	public void execute(BotUser user, String[] args, BotTextChannel channel) {
		// LanguageManager lang = getLanguageManager();
		if (args.length == 0) {
			reply("h!" + getUsage());
			return;
		}
		LanguageManager lang = getLanguageManager();
		Session session = HeroStatsBot.getPaladins().getSessionsCache().get(0);
		try {
			List<SearchPlayer> searchPlayer = (args.length >= 2)
					? session.getRequester().searchPlayer(args[0], Platform.getPlatformByName(args[1]))
					: session.getRequester().searchPlayer(args[0]);
			System.out.println(searchPlayer.toString());
			PaladinsPlayer player = session.getRequester().getPlayer(searchPlayer.get(0).getUserId()+"", null);
			List<ChampionRank> champranks = session.getRequester().getChampionRanks(player.getId());
			PlayerStatus status = session.getRequester().getPlayerStatus(player.getId() + "");
			InputStream input = StatusImage.drawImage(champranks.get(0), player);

			reply(new ContentMessage(embed(player, champranks, status).build(), input, "png"));

		} catch (PlayerNotFoundException e) {
			reply(TypeEmbed.simpleEmbed("^.^", lang.getString("Warnings", "INEXISTENT_USER")).build());
		}
	}

	public EmbedBuilder embed(PaladinsPlayer player, List<ChampionRank> rank, PlayerStatus status) {
		LanguageManager lang = getLanguageManager();
		EmbedBuilder embed = new EmbedBuilder();
		embed.setAuthor(player.getInGameName(), null, player.getAvatarURL());
		if (status.getStatus().name().equalsIgnoreCase("offline")) {
			embed.setDescription(
					Emoji.BLACK_CIRCLE.getAsMention() + " Status: " + statusconverter(status.getStatus()) + "\n");
		} else {
			embed.setDescription(":green_circle: Status: " + statusconverter(status.getStatus()) + "\n");
		}

		embed.appendDescription(
				Emoji.MAP.getAsMention() + lang.getString("StatusCommand", "REGION") + player.getRegion() + "\n");
		embed.appendDescription(
				Emoji.CYCLONE.getAsMention() + lang.getString("StatusCommand", "LEVEL") + player.getLevel() + "\n");
		embed.appendDescription(Emoji.CLOCK3.getAsMention() + lang.getString("StatusCommand", "PLAYEDTIME")
				+ player.getHoursPlayed() + " hora(s)\n");
		embed.appendDescription(Emoji.DOOR.getAsMention() + lang.getString("StatusCommand", "LASTLOGIN")
				+ DateFormat.getInstance().format(player.getLastLogin()) + "\n");

		int kills = 0;
		int deaths = 0;
		int assists = 0;
		for (ChampionRank r : rank) {
			kills += r.getKills();
			deaths += r.getDeaths();
			assists += r.getAssists();
		}
		embed.addField(lang.getString("StatusCommand", "STATISTICS"), kills + "/" + deaths + "/" + assists, true);
		embed.addField(lang.getString("StatusCommand", "WINSLOSSES"), player.getWins() + "/" + player.getLosses(),
				true);
		embed.addField(lang.getString("StatusCommand", "WINSRATE"),
				IntegerUtils.getPorcentagem(player.getWins() + player.getLosses(), player.getWins()), true);
		embed.setImage("attachment://" + "status" + ".png");
		String champicon = rank.get(0).getChampion().getChampionIcon();
		embed.setThumbnail(champicon);

		try {
			embed.setColor(ImageUtils.getPredominatColor(ImageIO.read(new URL(champicon))));
		} catch (IOException e) {
			embed.setColor(Color.YELLOW);
		}
		return embed;
	}

	public static String statusconverter(Status status) {
		if (status == Status.God_Selection) {
			return "Menu de Seleção";
		}
		if (status == Status.In_Game) {
			return "Em partida";
		}
		if (status == Status.In_Lobby) {
			return "No lobby";
		}
		if (status == Status.Unknown) {
			return "Desconhecido";
		}
		return status.name();

	}

}
