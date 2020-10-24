package me.skiincraft.discord.herostats.commands;

import com.google.gson.JsonObject;
import me.skiincraft.api.paladins.entity.player.Player;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.enums.Tier;
import me.skiincraft.api.paladins.exceptions.PlayerException;
import me.skiincraft.api.paladins.exceptions.SearchException;
import me.skiincraft.api.paladins.impl.PlayerImpl;
import me.skiincraft.api.paladins.objects.Place;
import me.skiincraft.api.paladins.objects.SearchPlayer;
import me.skiincraft.api.paladins.ranked.RankedKBM;
import me.skiincraft.discord.core.configuration.LanguageManager;
import me.skiincraft.discord.core.utils.IntegerUtils;
import me.skiincraft.discord.herostats.assets.PaladinsCommand;
import me.skiincraft.discord.herostats.exceptions.UnavailableAPIException;
import me.skiincraft.discord.herostats.utils.HeroUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

public class SplitCommand extends PaladinsCommand {

    public SplitCommand() {
        super("split", Collections.singletonList("elo"), "split <nickname> [platform]");
    }

    @Override
    public void execute(User user, String[] args, TextChannel channel) {
        if (args.length == 0){
            reply("h!" + getUsage());
            return;
        }

        try {
            SearchPlayer searchPlayer = (args.length >= 2)
                    ? searchPlayer(args[0], Platform.getPlatformByName(args[1]))
                    : searchPlayer(args[0], Platform.PC);

            if (searchPlayer.isPrivacyFlag()) {
                reply(TypeEmbed.privateProfile().build());
                return;
            }

            Player player = player(searchPlayer.getUserId());

            reply(splitEmbed(player, leaderboardPlace(player)).build());

        } catch (UnavailableAPIException apiException) {
            reply("A API do paladins se encontra indisponivel no momento.");
        } catch (PlayerException | SearchException e){
            reply(TypeEmbed.simpleEmbed(getLanguageManager().getString("Warnings", "T_INEXISTENT_USER"), getLanguageManager().getString("Warnings", "INEXISTENT_USER")).build());
        } catch (Exception e){
            reply(TypeEmbed.errorMessage(e, channel).build());
        }
    }

    public EmbedBuilder splitEmbed(Player player, Place place){
        RankedKBM rank = player.getRankedKBM();
        String placetext = (place == null) ? "" : " (" + place.getPosition() + "º)";
        EmbedBuilder embed = new EmbedBuilder();
        LanguageManager lang = getLanguageManager();

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
