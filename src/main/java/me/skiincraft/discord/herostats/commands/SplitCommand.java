package me.skiincraft.discord.herostats.commands;

import me.skiincraft.api.paladins.entity.player.Player;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.enums.Tier;
import me.skiincraft.api.paladins.exceptions.PlayerException;
import me.skiincraft.api.paladins.exceptions.SearchException;
import me.skiincraft.api.paladins.objects.Place;
import me.skiincraft.api.paladins.objects.SearchPlayer;
import me.skiincraft.api.paladins.ranked.RankedKBM;
import me.skiincraft.discord.core.command.InteractChannel;
import me.skiincraft.discord.core.configuration.LanguageManager;
import me.skiincraft.discord.herostats.assets.Category;
import me.skiincraft.discord.herostats.assets.PaladinsCommand;
import me.skiincraft.discord.herostats.exceptions.UnavailableAPIException;
import me.skiincraft.discord.herostats.utils.HeroUtils;
import me.skiincraft.discord.herostats.utils.IntegerUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.util.Collections;

public class SplitCommand extends PaladinsCommand {

    public SplitCommand() {
        super("split", Collections.singletonList("elo"), "split <nickname> [platform]");
    }

    public Category category() {
        return Category.Ranking;
    }

    @Override
    public void execute(Member user, String[] args, InteractChannel channel) {
        if (args.length == 0){
            channel.reply("h!" + getUsage());
            return;
        }

        try {
            SearchPlayer searchPlayer = (args.length >= 2)
                    ? searchPlayer(args[0], Platform.getPlatformByName(args[1]))
                    : searchPlayer(args[0], Platform.PC);

            if (searchPlayer.isPrivacyFlag()) {
                channel.reply(TypeEmbed.privateProfile().build());
                return;
            }

            Player player = player(searchPlayer.getUserId());

            channel.reply(splitEmbed(player, leaderboardPlace(player), getLanguageManager(channel.getTextChannel().getGuild())).build());

        } catch (UnavailableAPIException apiException) {
            channel.reply("A API do paladins se encontra indisponivel no momento.");
        } catch (PlayerException | SearchException e){
            channel.reply(TypeEmbed.simpleEmbed(getLanguageManager(channel.getTextChannel().getGuild()).getString("Warnings", "T_INEXISTENT_USER"), getLanguageManager(channel.getTextChannel().getGuild()).getString("Warnings", "INEXISTENT_USER")).build());
        } catch (Exception e){
            channel.reply(TypeEmbed.errorMessage(e, channel.getTextChannel()).build());
        }
    }

    public EmbedBuilder splitEmbed(Player player, Place place, LanguageManager lang){
        RankedKBM rank = player.getRankedKBM();
        String placetext = (place == null) ? "" : " (" + place.getPosition() + "º)";
        EmbedBuilder embed = new EmbedBuilder();

        Tier tier = (place == null) ? player.getTier() : place.getTier();

        embed.setTitle("Classificação da Temporada");
        embed.setAuthor(player.getInGameName(), null, (player.getAvatarId() != 0)
                ? player.getAvatarURL() : HeroUtils.getTierImage(tier));

        embed.setDescription(":trophy: " + lang.getString("StatusCommand", "WINSLOSSES") + ": " + rank.getWins() + "/" + rank.getLosses()+ "\n");
        embed.appendDescription(":chart_with_upwards_trend: " + lang.getString("StatusCommand", "WINSRATE") + ": " + IntegerUtils.getPorcentagem(rank.getWins() + rank.getLosses(), rank.getWins()) + "\n");
        embed.appendDescription(":cyclone: " + lang.getString("StatusCommand", "LEVEL") + player.getLevel() + "\n");

        embed.addField(lang.getString("SplitCommand", "TIER"), tier.getName(Language.Portuguese) + placetext, true);
        embed.addField(lang.getString("SplitCommand", "POINTS"), player.getRankedKBM().getPoints() + " PT", true);
        embed.addBlankField(true);

        embed.setThumbnail(HeroUtils.getTierImage(tier));
        embed.setColor(new Color(255, 241, 102));

        return embed;
    }


}
