package me.skiincraft.discord.herostats.commands;

import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.match.HistoryMatch;
import me.skiincraft.api.paladins.entity.match.MatchPlayer;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.exceptions.MatchException;
import me.skiincraft.api.paladins.exceptions.SearchException;
import me.skiincraft.api.paladins.objects.SearchPlayer;
import me.skiincraft.discord.core.configuration.LanguageManager;
import me.skiincraft.discord.herostats.assets.Category;
import me.skiincraft.discord.herostats.assets.PaladinsCommand;
import me.skiincraft.discord.herostats.exceptions.UnavailableAPIException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HistoryCommand extends PaladinsCommand {

    public HistoryCommand() {
        super("history", Collections.singletonList("historico"), "history <player> [platform]");
    }

    public Category category() {
        return Category.Match;
    }

    @Override
    public void execute(User user, String[] args, TextChannel channel) {
        if (args.length == 0){
            reply("!h" + getUsage());
            return;
        }
        try {
            SearchPlayer searchPlayer = (args.length >= 2)
                    ? searchPlayer(args[0], Platform.getPlatformByName(args[1]))
                    : searchPlayer(args[0], Platform.PC);

            if (searchPlayer.isPrivacyFlag()) {
                reply("Esse jogador se encontra com o perfil privado.");
                return;
            }

            reply(TypeEmbed.processing().build(), message -> {
                List<HistoryMatch> history = endpont().getMatchHistory(searchPlayer.getUserId()).get();

            });
        } catch (Exception e){
            reply(TypeEmbed.errorMessage(e, channel).build());
        }
    }

    public EmbedBuilder embed(HistoryMatch match, SearchPlayer player){
        EmbedBuilder embed = new EmbedBuilder();
        MatchPlayer matchPlayer = match.getMatchPlayer();

        LanguageManager lang = getLanguageManager();

        Champion principalChampion = matchPlayer.getChampion(Language.Portuguese).get();

        embed.setAuthor(matchPlayer.getName(), null, principalChampion.getIcon());
        embed.setTitle("Historico de Partida");
        String description = stringWrap(
                ":trophy: Resultado: " + winnerConvert(match.getWinner()),
                ":id: Identificação: `" + match.getMatchId() + "`",
                ":game_die: Modo de Jogo: " + ((match.isRanked()) ? "Competitivo" : "Casual"));

        embed.addField("Estatistica:", stringWrap(
                "KDA: " + matchPlayer.getKillsRaw() + "/",
                ""
        ), true);
        embed.setDescription(description);

        return embed;
    }

    public String winnerConvert(String string){
        return (string.equalsIgnoreCase("Win")) ? "Vitória" : "Derrota";
    }

    public String stringWrap(String... string){
        return String.join(" \n", string);
    }

}
