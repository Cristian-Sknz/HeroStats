package me.skiincraft.discord.herostats.commands;

import java.awt.Color;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import me.skiincraft.api.paladins.utils.AccessUtils;
import me.skiincraft.discord.herostats.HeroStatsBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class TypeEmbed {

	private static final String defaultPrefix = "h!";

	public static EmbedBuilder errorMessage(Exception exception, TextChannel channel) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(":tickets: Ocorreu um problema");
		embed.setAuthor("HeroStats - Discord Bot", null, channel.getJDA().getSelfUser().getAvatarUrl());
		embed.addField("Infelizmente ocorreu um problema", "```css\n" + generateStringException(exception) +"```", false);
		embed.setFooter("Caso persistir esse erro, entre em contato com o desenvolvedor", "https://i.imgur.com/vt49jhG.png");
		embed.setColor(new Color(18, 138, 133));
		embed.setTimestamp(OffsetDateTime.now(Clock.systemUTC()));

		exception.printStackTrace();

		return embed;
	}

	private static String generateStringException(Exception e){
		StringBuilder builder = new StringBuilder();
		List<StackTraceElement> allElements = new ArrayList<>(Arrays.asList(e.getStackTrace()));
		List<StackTraceElement> traceElements = allElements.stream()
				.filter(trace -> trace.toString().contains("me.skiincraft.discord"))
				.collect(Collectors.toList());
		builder.append(e.getLocalizedMessage()).append("\n");

		if (traceElements.size() == 0){
			for (StackTraceElement element : allElements){
				if (allElements.get(allElements.size() - 1) == element) {
					builder.append("     in ").append(element.toString()).append("\n");
					break;
				}
				builder.append("   at ").append(element.toString()).append("\n");
			}
		}

		if (traceElements.size() != 0) {
			for (StackTraceElement element : allElements) {
				if (traceElements.get(traceElements.size() - 1) == element) {
					builder.append("     in ").append(element.toString()).append("\n");
					break;
				}
				builder.append("   at ").append(element.toString()).append("\n");
			}
		}
		return builder.toString();
	}

	public static EmbedBuilder processing() {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Processando!");
		embed.setDescription("\nAguarde um momento.");
		embed.setTimestamp(OffsetDateTime.now(Clock.systemUTC()));
		embed.setColor(Color.GREEN);
		return embed;
	}

	public static EmbedBuilder privateProfile() {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("<:dnd2:769610221655425122> Perfil privado!");
		embed.setDescription("Este usuário que você solicitou tem o perfil privado.");
		embed.setColor(new Color(255, 137, 81));
		embed.setFooter("Caso tenha o perfil privado, desabilite-o para mostrar.");

		return embed;
	}

	private static String randomHelpImage() {
		Random r = new Random();
		int i = r.nextInt(3);

		return (i == 1) ? "https://i.imgur.com/bz1MKtv.jpg" : "https://i.imgur.com/pkvpKuJ.jpg";
	}
	
	public static EmbedBuilder simpleEmbed(String title, String description) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(title);
		embed.setDescription(description);
		embed.setColor(Color.ORANGE);
		return embed;
	}

	public static EmbedBuilder WarningEmbed(String title, String description) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(title);
		embed.setDescription(description);

		embed.setThumbnail("https://i.imgur.com/4ZkdIyq.png");// warning
		embed.setColor(Color.RED);
		embed.setFooter(defaultPrefix + "help to help!");

		return embed;
	}

	public static EmbedBuilder SoftWarningEmbed(String title, String description) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(title);
		embed.setDescription(description);

		embed.setThumbnail("https://i.imgur.com/mG7BgFg.png"); // Hatsunemiku chibi
		embed.setColor(new Color(222, 74, 0));// Orange+-

		embed.setFooter(defaultPrefix + "help to help!");
		return embed;
	}

	public static EmbedBuilder HelpEmbed(String title, String description) {
		EmbedBuilder embed = new EmbedBuilder();

		embed.setTitle(title);
		embed.setDescription(description);

		embed.setThumbnail(randomHelpImage());
		embed.setColor(Color.YELLOW);
		embed.setFooter(defaultPrefix + "help to help!");

		return embed;
	}

	public static EmbedBuilder InfoEmbed(String title, String description) {
		EmbedBuilder embed = new EmbedBuilder();

		embed.setTitle(title);
		embed.setDescription(description);

		embed.setThumbnail("https://i.imgur.com/QsOc85X.gif");
		embed.setColor(new Color(158, 158, 158));// Cinza
		embed.setFooter(defaultPrefix + "help to help!");

		return embed;
	}

	public static EmbedBuilder ConfigEmbed(String title, String description) {
		EmbedBuilder embed = new EmbedBuilder();

		embed.setTitle(title);
		embed.setDescription(description);

		embed.setThumbnail("https://i.imgur.com/SSSHW6P.png");
		embed.setColor(new Color(158, 158, 158));// Cinza
		embed.setFooter(defaultPrefix + "help to help!");

		return embed;
	}

	public static EmbedBuilder DefaultEmbed(String title, String description) {
		EmbedBuilder b = new EmbedBuilder();
		// User user =
		// HeroStatsBot.getMain().getShardManager().getUserById("247096601242238991");

		b.setColor(new Color(255, 140, 0));
		b.setTitle(title);
		b.setDescription(description);
		b.setFooter("HeroStats 2020.");
		return b;
	}

	public static EmbedBuilder LoadingEmbed() {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Loading...");
		embed.setColor(Color.YELLOW);
		embed.setThumbnail("https://i.imgur.com/kPLyktW.gif");
		return embed;
	}

}
