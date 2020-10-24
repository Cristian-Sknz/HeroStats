package me.skiincraft.discord.herostats.commands;

import me.skiincraft.api.paladins.common.EndPoint;
import me.skiincraft.api.paladins.common.Request;
import me.skiincraft.api.paladins.entity.player.Player;
import me.skiincraft.api.paladins.entity.player.PlayerChampion;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.enums.PlayerStatus;
import me.skiincraft.api.paladins.enums.PlayerStatus.Status;
import me.skiincraft.api.paladins.exceptions.PlayerException;
import me.skiincraft.api.paladins.exceptions.SearchException;
import me.skiincraft.api.paladins.objects.SearchPlayer;
import me.skiincraft.discord.core.command.ContentMessage;
import me.skiincraft.discord.core.configuration.LanguageManager;
import me.skiincraft.discord.core.utils.Emoji;
import me.skiincraft.discord.core.utils.ImageUtils;
import me.skiincraft.discord.core.utils.IntegerUtils;
import me.skiincraft.discord.herostats.HeroStatsBot;
import me.skiincraft.discord.herostats.assets.PaladinsCommand;
import me.skiincraft.discord.herostats.imagebuild.StatusImage;
import me.skiincraft.discord.herostats.utils.HeroUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class StatusCommand extends PaladinsCommand {

	public StatusCommand() {
		super("status", Collections.singletonList("stats"), "status <nickname> [platform]");
	}

	@Override
	public void execute(User user, String[] args, TextChannel channel) {
		// LanguageManager lang = getLanguageManager();
		if (args.length == 0) {
			reply("h!" + getUsage());
			return;
		}

		LanguageManager lang = getLanguageManager();
		EndPoint endPoint = HeroStatsBot.getPaladins().getSessions().get(0).getEndPoint();
		try {
			SearchPlayer searchPlayer = (args.length >= 2)
					? searchPlayer(args[0], Platform.getPlatformByName(args[1]))
					: searchPlayer(args[0], Platform.PC);

			if (searchPlayer.isPrivacyFlag()) {
				reply(TypeEmbed.privateProfile().build());
				return;
			}

			reply(TypeEmbed.processing().build(), processing -> {
				Request<Player> request = endPoint.getPlayer(searchPlayer.getUserId());
				Player player = request.get();

				request.getWithJson((p, j) -> {
					try {
						HeroUtils.createFile(j, p.getName() + "1");
					} catch (IOException e) {
						e.printStackTrace();
					}
				});


				List<PlayerChampion> champranks = endPoint.getPlayerChampions(player.getId()).get().getAsList();
				PlayerStatus status = endPoint.getPlayerStatus(player.getId() + "").get();
				InputStream input = StatusImage.drawImage(champranks.get(0), player);

				reply(new ContentMessage(embed(player, champranks, status).build(), input, "png"));
				processing.delete().queue();
			});
		} catch (PlayerException e) {
			reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_INEXISTENT_USER"), lang.getString("Warnings", "INEXISTENT_USER")).build());
		} catch (SearchException e) {
			reply(TypeEmbed.simpleEmbed(getLanguageManager().getString("Warnings", "T_INEXISTENT_USER"), getLanguageManager().getString("Warnings", "INEXISTENT_USER")).build());
		} catch (Exception e){
			reply(TypeEmbed.errorMessage(e, channel).build());
		}
	}

	public EmbedBuilder embed(Player player, List<PlayerChampion> rank, PlayerStatus status) {
		LanguageManager lang = getLanguageManager();
		EmbedBuilder embed = new EmbedBuilder();
		String champicon = rank.get(0).getChampion(Language.Portuguese).get().getIcon();
		embed.setAuthor(player.getInGameName(), null, (player.getAvatarId() != 0)
				? player.getAvatarURL() : champicon);
		if (status.getStatus().name().equalsIgnoreCase("offline")) {
			embed.setDescription(
					Emoji.BLACK_CIRCLE.getAsMention() + " Status: " + statusconverter(status.getStatus()) + "\n");
		} else {
			embed.setDescription(":green_circle: Status: " + statusconverter(status.getStatus()) + "\n");
		}

		embed.appendDescription(":map: " + lang.getString("StatusCommand", "REGION") + player.getRegion() + "\n");
		embed.appendDescription(":cyclone: " + lang.getString("StatusCommand", "LEVEL") + player.getLevel() + "\n");
		embed.appendDescription(":clock3: " + lang.getString("StatusCommand", "PLAYEDTIME")
				+ player.getHoursPlayed() + " hora(s)\n");

		int kills = 0;
		int deaths = 0;
		int assists = 0;
		for (PlayerChampion r : rank) {
			kills += r.getKills();
			deaths += r.getDeaths();
			assists += r.getAssists();
		}

		embed.addField(lang.getString("StatusCommand", "STATISTICS"), kills + "/" + deaths + "/" + assists, true);
		embed.addField(lang.getString("StatusCommand", "WINSLOSSES"),
				player.getWins() + "/" + player.getLosses()
						+ " - " + IntegerUtils.getPorcentagem(player.getWins() + player.getLosses(), player.getWins()),
				true);
		embed.setImage("attachment://" + "status" + ".png");
		embed.setThumbnail(champicon);
		embed.setFooter("Ultima vez online ", champicon);
		embed.setTimestamp(player.getLastLogin());

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
