package me.skiincraft.discord.herostats.commands;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.skiincraft.api.paladins.Queue;
import me.skiincraft.api.paladins.common.Champion;
import me.skiincraft.api.paladins.common.ChampionLoadout;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.exceptions.PlayerNotFoundException;
import me.skiincraft.api.paladins.objects.SearchPlayer;
import me.skiincraft.discord.core.commands.Command;
import me.skiincraft.discord.core.entity.BotTextChannel;
import me.skiincraft.discord.core.entity.BotUser;
import me.skiincraft.discord.core.entity.ContentMessage;
import me.skiincraft.discord.core.multilanguage.LanguageManager;
import me.skiincraft.discord.core.reactions.ReactionUtil;
import me.skiincraft.discord.herostats.HeroStatsBot;
import me.skiincraft.discord.herostats.imagebuild.DeckPreviewImage;
import net.dv8tion.jda.api.EmbedBuilder;

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
		return list.stream().filter(o -> o.getChampionName().equalsIgnoreCase(name)).findAny().orElse(null);
	}

	@Override
	public void execute(BotUser user, String[] args, BotTextChannel channel) {
		LanguageManager lang = getLanguageManager();
		if (args.length <= 1) {
			// TODO sendusage
			reply("h!" + getUsage());
			return;
		}
		Queue queue = HeroStatsBot.getPaladins().getSessionsCache().get(0).getRequester();
		List<String> championsname = new ArrayList<>();
		for (Champion champ : queue.getLoadedchampions()) {
			championsname.add(champ.getChampionName());
		}

		String champ = existsChampion(championsname, args[1]);
		Champion champion = getChampion(queue.getLoadedchampions(), champ);
		if (champ == null) {
			reply(TypeEmbed.simpleEmbed("^.^", lang.getString("Warnings", "INEXISTENT_CHAMPION")).build());
			return;
		}

		try {
			List<SearchPlayer> searchPlayer = (args.length == 3)
					? queue.searchPlayer(args[0], Platform.getPlatformByName(args[2]))
					: queue.searchPlayer(args[0]);
			List<ChampionLoadout> loadout = queue.getChampionsLoadouts(searchPlayer.get(0).getUserId(),
					(getLanguageManager().getLanguage().name() == "Portuguese") ? Language.Portuguese
							: Language.English);
			List<ChampionLoadout> decks = new ArrayList<>();

			for (ChampionLoadout champdeck : loadout) {
				if (champdeck.getChampionId() == champion.getChampionId()) {
					decks.add(champdeck);
				}
			}

			if (decks.size() == 0) {
				// TODO mensagem de loadout não existente
				reply(TypeEmbed.simpleEmbed("^.^", lang.getString("Warnings", "INEXISTENT_LOADOUT")).build());
				return;
			}

			InputStream input = DeckPreviewImage.drawImage(decks.get(0), getLanguageManager());
			reply(new ContentMessage(embed(decks.get(0)).build(), input, "png"), o ->{
				o.addReaction("U+1F4F0").queue();
				HeroStatsBot.simples.add(new ReactionUtil(user.getId(), o.getIdLong(), o.getGuild().getIdLong(), null));
			});
		} catch (PlayerNotFoundException e) {
			reply(TypeEmbed.simpleEmbed("^.^", lang.getString("Warnings", "INEXISTENT_USER")).build());
		}
	}

	public EmbedBuilder embed(ChampionLoadout loadouts) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setAuthor(loadouts.getDeckOwner(), null, loadouts.getChampion().getChampionIcon());
		embed.setTitle("Visualização de Decks");
		embed.setThumbnail(loadouts.getChampion().getChampionIcon());

		embed.setImage("attachment://" + "deck" + ".png");

		embed.setDescription("<:cards:728729369756958750>" + "Nome do Deck: " + loadouts.getDeckname());
		embed.appendDescription(
				"\n" + "<:championemote:727241756281929729>" + "Campeão: " + loadouts.getChampionName());
		embed.appendDescription("\n:id: Identificação: " + loadouts.getDeckId());

		return embed;
	}

}
