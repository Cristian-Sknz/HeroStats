package me.skiincraft.discord.herostats.commands;

import java.awt.Color;
import java.util.Random;

import net.dv8tion.jda.api.EmbedBuilder;

public class TypeEmbed {

	private static String defaultPrefix = "h!";

	private static String randomHelpImage() {
		Random r = new Random();
		int i = r.nextInt(2);

		return (i == 0) ? "https://i.imgur.com/bz1MKtv.jpg" : (i == 1) ? "https://i.imgur.com/pkvpKuJ.jpg" : null;
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
