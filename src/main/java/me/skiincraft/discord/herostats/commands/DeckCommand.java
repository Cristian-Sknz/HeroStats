package me.skiincraft.discord.herostats.commands;

import me.skiincraft.api.paladins.common.EndPoint;
import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.player.Loadout;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.exceptions.PlayerException;
import me.skiincraft.api.paladins.exceptions.SearchException;
import me.skiincraft.api.paladins.objects.SearchPlayer;
import me.skiincraft.discord.core.command.ContentMessage;
import me.skiincraft.discord.core.configuration.LanguageManager;
import me.skiincraft.discord.herostats.HeroStatsBot;
import me.skiincraft.discord.herostats.assets.PaladinsCommand;
import me.skiincraft.discord.herostats.imagebuild.DeckPreviewImage;
import me.skiincraft.discord.herostats.listeners.DeckChooser;
import me.skiincraft.discord.herostats.listeners.DeckChooserObject;
import me.skiincraft.discord.herostats.utils.HeroUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DeckCommand extends PaladinsCommand {

	public DeckCommand() {
		super("deck", Arrays.asList("loadout", "baralho"), "deck <player> <champion> [platform]");
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
			// TODO sendusage
			reply("h!" + getUsage());
			return;
		}
		EndPoint queue = HeroStatsBot.getPaladins().getSessions().get(0).getEndPoint();
		List<Champion> champions = champions(Language.Portuguese).getAsList();

		String[] newArgs = replaceSpaceChamps(args);

		Champion champion = champions.stream().filter(o -> o.getName().equalsIgnoreCase(newArgs[1].replace("_", " "))).findAny().orElse(null);
		if (champion == null) {
			reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_INEXISTENT_CHAMPION"), lang.getString("Warnings", "INEXISTENT_CHAMPION")).build());
			return;
		}

		try {
			SearchPlayer searchPlayer = (newArgs.length == 3)
					? searchPlayer(newArgs[0], Platform.getPlatformByName(newArgs[2]))
					: searchPlayer(newArgs[0], Platform.PC);

			if (searchPlayer.isPrivacyFlag()) {
				reply(TypeEmbed.privateProfile().build());
				return;
			}
					
			List<Loadout> loadout = queue.getLoadouts(searchPlayer.getUserId(), Language.Portuguese).get().getAsList();
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
				reply(deckChooser(decks).build(), message -> DeckChooser.objects.add(new DeckChooserObject(user.getIdLong(), channel, decks, message)));
				return;
			}

			InputStream input = DeckPreviewImage.drawImage(decks.get(0));
			reply(new ContentMessage(embed(decks.get(0)).build(), input, "png"));
		} catch (SearchException | PlayerException e) {
			reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_INEXISTENT_USER"), lang.getString("Warnings", "INEXISTENT_USER")).build());
		} catch (Exception e){
			reply(TypeEmbed.errorMessage(e, channel).build());
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
		embed.setColor(new Color(117, 66, 187));

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
		embed.appendDescription("\n:id: Identificação: `" + loadouts.getDeckId()+ "`");

		embed.setColor(HeroUtils.paladinsClassColor(champion));
		embed.setFooter("Visualização por HeroStats Bot", champion.getIcon());
		embed.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC));

		return embed;
	}

}
