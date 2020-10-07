package me.skiincraft.discord.herostats.commands;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import me.skiincraft.api.paladins.common.EndPoint;
import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.player.Loadout;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.objects.SearchPlayer;
import me.skiincraft.discord.core.command.Command;
import me.skiincraft.discord.core.command.ContentMessage;
import me.skiincraft.discord.core.configuration.LanguageManager;
import me.skiincraft.discord.herostats.HeroStatsBot;
import me.skiincraft.discord.herostats.imagebuild.DeckPreviewImage;
import me.skiincraft.discord.herostats.listeners.DeckChooser;
import me.skiincraft.discord.herostats.listeners.DeckChooserObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class DeckCommand extends Command {

	public DeckCommand() {
		super("deck", Arrays.asList("loadout", "baralho"), "deck <player> <champion> [platform]");
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
		if (name.toLowerCase() == "maldamba") {
			return "Mal'Damba";
		}
		if (name.toLowerCase() == "damba") {
			return "Mal'Damba";
		}
		return list.stream().filter(o -> o.toLowerCase().contains(name.toLowerCase())).findAny().orElse(null);
	}

	public Champion getChampion(List<Champion> list, final String name) {
		return list.stream().filter(o -> o.getName().equalsIgnoreCase(name)).findAny().orElse(null);
	}

	@Override
	public void execute(User user, String[] args, TextChannel channel) {
		LanguageManager lang = getLanguageManager();
		if (args.length <= 1) {
			// TODO sendusage
			reply("h!" + getUsage());
			return;
		}
		EndPoint queue = HeroStatsBot.getPaladins().getSessions().get(0).getEndPoint();
		List<Champion> champions = queue.getChampions(Language.Portuguese).get().getAsList();
		List<String> championsname = champions.stream().map(i -> i.getName()).collect(Collectors.toList());

		String champ = existsChampion(championsname, args[1]);
		Champion champion = getChampion(champions, champ);
		if (champ == null) {
			reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_INEXISTENT_CHAMPION"), lang.getString("Warnings", "INEXISTENT_CHAMPION")).build());
			return;
		}

		try {
			List<SearchPlayer> searchPlayer = (args.length == 3)
					? queue.searchPlayer(args[0], Platform.getPlatformByName(args[2])).get().getAsList()
					: queue.searchPlayer(args[0], Platform.PC).get().getAsList();
					
			List<Loadout> loadout = queue.getLoadouts(searchPlayer.get(0).getUserId(), Language.Portuguese).get().getAsList();
			List<Loadout> decks = new ArrayList<>();

			for (Loadout champdeck : loadout) {
				if (champdeck.getChampionId() == champion.getId()) {
					decks.add(champdeck);
				}
			}

			if (decks.size() == 0) {
				reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_INEXISTENT_LOADOUT"), lang.getString("Warnings", "INEXISTENT_LOADOUT")).build());
				return;
			}

			if (decks.size() > 1){
				reply(deckChooser(decks).build(), message -> {
					DeckChooser.objects.add(new DeckChooserObject(user.getIdLong(), channel, decks, message));
				});
				return;
			}

			InputStream input = DeckPreviewImage.drawImage(decks.get(0), getLanguageManager());
			reply(new ContentMessage(embed(decks.get(0)).build(), input, "png"));
		} catch (Exception e) {
			e.printStackTrace();
			reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_INEXISTENT_USER"), lang.getString("Warnings", "INEXISTENT_USER")).build());
		}
	}

	public EmbedBuilder deckChooser(List<Loadout> decks){
		EmbedBuilder embed = new EmbedBuilder();
		Champion champion = decks.get(0).getChampion().get();
		embed.setAuthor(decks.get(0).getOwnername(), null, champion.getIcon());
		embed.setTitle("Escolha um Baralho: ");
		embed.setThumbnail(champion.getIcon());
		StringBuilder builder = new StringBuilder();

		AtomicInteger num = new AtomicInteger();
		decks.stream()
				.map(Loadout::getDeckname)
				.map(name -> (num.getAndIncrement() + 1) + " - " + name)
				.forEach(deck -> {
					builder.append(deck);
					builder.append("\n");
				});

		embed.setDescription(builder.toString());

		return embed;
	}

	public static EmbedBuilder embed(Loadout loadouts) {
		EmbedBuilder embed = new EmbedBuilder();
		Champion champion = loadouts.getChampion().get();
		embed.setAuthor(loadouts.getOwnername(), null, champion.getIcon());
		embed.setTitle("Visualização de Decks");
		embed.setThumbnail(champion.getIcon());

		embed.setImage("attachment://" + "deck" + ".png");

		embed.setDescription("<:cards:728729369756958750>" + "Nome do Deck: " + loadouts.getDeckname());
		embed.appendDescription(
				"\n" + "<:championemote:727241756281929729>" + "Campeão: " + loadouts.getChampionName());
		embed.appendDescription("\n:id: Identificação: " + loadouts.getDeckId());

		return embed;
	}

}
