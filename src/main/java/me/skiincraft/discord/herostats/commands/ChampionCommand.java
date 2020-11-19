package me.skiincraft.discord.herostats.commands;

import me.skiincraft.api.paladins.common.EndPoint;
import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.player.PlayerChampion;
import me.skiincraft.api.paladins.entity.player.objects.PlayerChampions;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.exceptions.SearchException;
import me.skiincraft.api.paladins.objects.SearchPlayer;
import me.skiincraft.discord.core.command.InteractChannel;
import me.skiincraft.discord.core.common.chooser.ChooserObject;
import me.skiincraft.discord.core.configuration.LanguageManager;
import me.skiincraft.discord.herostats.HeroStatsBot;
import me.skiincraft.discord.herostats.assets.Category;
import me.skiincraft.discord.herostats.assets.PaladinsCommand;
import me.skiincraft.discord.herostats.choosers.ChampionChooser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

public class ChampionCommand extends PaladinsCommand {

	public ChampionCommand() {
		super("champion", Arrays.asList("campeão", "campeao", "champ"), "champion <user> <champion> [platform]");
	}

	public Category category() {
		return Category.Statistics;
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
				.replace("maindosknz", "ash")
				.split(" ");
	}


	@Override
	public void execute(Member user, String[] args, InteractChannel channel) {
		LanguageManager lang = getLanguageManager(channel.getTextChannel().getGuild());
		if (args.length <= 1) {
			channel.reply("h!"+ getUsage());
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
			channel.reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_INEXISTENT_CHAMPION"), lang.getString("Warnings", "INEXISTENT_CHAMPION")).build());
			return;
		}

		try {
			SearchPlayer searchPlayer = (newArgs.length == 3)
					? searchPlayer(newArgs[0], Platform.getPlatformByName(newArgs[2]))
					: searchPlayer((newArgs[0]), Platform.PC);

			if (searchPlayer.isPrivacyFlag()) {
				channel.reply(TypeEmbed.privateProfile().build());
				return;
			}

			channel.reply(embedChoice(champ, searchPlayer).build(), botmessage -> {
				ChampionChooser chooser = new ChampionChooser(channel.getTextChannel(), botmessage, requester, searchPlayer);
				chooser.setChampion(champ);
				ChooserObject object = new ChooserObject(user.getIdLong(), channel.getTextChannel(), new String[]{"1", "2", "3", "4", "5", "cancel"});

				HeroStatsBot.getChooser().registerChooser(object, chooser);
			});
		} catch (SearchException e) {
			channel.reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_INEXISTENT_USER"), lang.getString("Warnings", "INEXISTENT_USER")).build());
		} catch (Exception e){
			channel.reply(TypeEmbed.errorMessage(e, channel.getTextChannel()).build());
		}
	}

	public EmbedBuilder embedChoice(Champion champion, SearchPlayer searchPlayer){
		EmbedBuilder embed = new EmbedBuilder();
		embed.setAuthor(searchPlayer.getInGameName(),null,champion.getIcon());
		embed.setTitle("Selecione um modo:");
		embed.setDescription(":one: - Todos os modos;\n")
				.appendDescription(":two: - Competitivo\n")
				.appendDescription(":three: - Cerco\n")
				.appendDescription(":four: - Deathmatch\n")
				.appendDescription(":five: - Chacina\n");

		embed.setThumbnail(champion.getIcon());
		embed.setFooter("Caso queira cancelar digite: cancel");
		embed.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC));

		return embed;
	}
}
