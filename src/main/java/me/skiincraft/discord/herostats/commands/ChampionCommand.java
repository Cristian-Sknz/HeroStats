package me.skiincraft.discord.herostats.commands;

import me.skiincraft.api.paladins.common.EndPoint;
import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.player.PlayerChampion;
import me.skiincraft.api.paladins.entity.player.objects.PlayerChampions;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.exceptions.SearchException;
import me.skiincraft.api.paladins.objects.SearchPlayer;
import me.skiincraft.discord.core.configuration.LanguageManager;
import me.skiincraft.discord.herostats.HeroStatsBot;
import me.skiincraft.discord.herostats.assets.PaladinsCommand;
import me.skiincraft.discord.herostats.listeners.ChampionChoiceObject;
import me.skiincraft.discord.herostats.listeners.ChampionChooser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

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

		try {
			SearchPlayer searchPlayer = (newArgs.length == 3)
					? searchPlayer(newArgs[0], Platform.getPlatformByName(newArgs[2]))
					: searchPlayer((newArgs[0]), Platform.PC);

			if (searchPlayer.isPrivacyFlag()) {
				reply(TypeEmbed.privateProfile().build());
				return;
			}

			reply(embedChoice(champ, searchPlayer).build(), message -> ChampionChooser.objects.add(new ChampionChoiceObject(user.getIdLong(), channel, message, champ, searchPlayer, requester)));
		} catch (SearchException e) {
			reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_INEXISTENT_USER"), lang.getString("Warnings", "INEXISTENT_USER")).build());
		} catch (Exception e){
			reply(TypeEmbed.errorMessage(e, channel).build());
		}
	}

	public EmbedBuilder embedChoice(Champion champion, SearchPlayer searchPlayer){
		EmbedBuilder embed = new EmbedBuilder();
		embed.setAuthor(searchPlayer.getInGameName(),null,champion.getIcon());
		embed.setDescription(":one: - Todos os modos;\n")
				.appendDescription(":two: - Competitivo;\n")
				.appendDescription(":three: - Cerco;\n")
				.appendDescription(":four: - Deathmatch;\n")
				.appendDescription(":five: - Chacina.\n");

		embed.setThumbnail(champion.getIcon());
		embed.setFooter("Caso queira cancelar digite: 6");
		embed.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC));

		return embed;
	}
}
