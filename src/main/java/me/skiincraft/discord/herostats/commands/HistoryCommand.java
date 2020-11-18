package me.skiincraft.discord.herostats.commands;

import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.match.HistoryMatch;
import me.skiincraft.api.paladins.entity.match.MatchPlayer;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.exceptions.MatchException;
import me.skiincraft.api.paladins.exceptions.SearchException;
import me.skiincraft.api.paladins.objects.SearchPlayer;
import me.skiincraft.discord.core.common.reactions.ReactionObject;
import me.skiincraft.discord.core.common.reactions.Reactions;
import me.skiincraft.discord.core.common.reactions.custom.ReactionPage;
import me.skiincraft.discord.herostats.assets.Category;
import me.skiincraft.discord.herostats.assets.PaladinsCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HistoryCommand extends PaladinsCommand {

    public HistoryCommand() {
        super("history", Collections.singletonList("historico"), "history <player> [champion/platform]");
    }

    public Category category() {
        return Category.Match;
    }

    private String[] replaceSpaceChamps(String[] string){
        return String.join(" ", string)
                .toLowerCase()
                .replace("sha lin", "sha_lin")
                .replace("bomb king", "bomb_king")
                .replace("mal damba", "mal'damba")
                .replace("maldamba", "mal'damba")
                .replace("bk", "bomb_king")
                .split(" ");
    }

    @Override
    public void execute(User user, String[] args, TextChannel channel) {
        if (args.length == 0) {
            reply("h!" + getUsage());
            return;
        }
        String[] newArgs = replaceSpaceChamps(args);
        try {
            SearchPlayer searchPlayer = (newArgs.length >= 2)
                    ? searchPlayer(newArgs[0], (newArgs.length >= 3) ? Platform.getPlatformByName(newArgs[2]) : Platform.getPlatformByName(newArgs[1]))
                    : searchPlayer(newArgs[0], Platform.PC);

            if (searchPlayer.isPrivacyFlag()) {
                reply(TypeEmbed.privateProfile().build());
                return;
            }

            reply(TypeEmbed.processing().build(), message -> {
                List<HistoryMatch> history = endpont().getMatchHistory(searchPlayer.getUserId()).get();

                if (newArgs.length >= 3){
                    Champion champ = champions(Language.Portuguese)
                            .getAsStream()
                            .filter(o -> o.getName().equalsIgnoreCase(newArgs[1].replace("_", " ")))
                            .findAny()
                            .orElse(null);
                    if (champ != null){
                        history = history.stream().filter(c -> c.getMatchPlayer().getChampionId() == champ.getId()).collect(Collectors.toList());
                        if (history.size() == 0){
                            message.editMessage(TypeEmbed.DefaultEmbed("Não foi possivel encontrar nenhuma partida", "Não consegui achar este campeão no historico.").build()).queue();
                            return;
                        }
                    }
                }

                List<EmbedBuilder> embeds = new ArrayList<>();
                int i = 1;
                for (HistoryMatch match : history) {
                    embeds.add(embed(match, i, history.size()));
                    i++;
                }

                message.editMessage(embeds.get(0).build()).queue();
                Objects.requireNonNull(Reactions.getInstance()).registerReaction(new ReactionObject(message, user.getIdLong(), new String[]{"U+25C0", "U+25B6"}),
                        new ReactionPage(embeds, true));
            });
        } catch (MatchException e) {
            reply(TypeEmbed.DefaultEmbed("Não foi possivel encontrar nenhuma partida", "Algo aconteceu, e não foi possivel pegar as partidas").build());
         } catch (SearchException e) {
            reply(TypeEmbed.simpleEmbed(getLanguageManager().getString("Warnings", "T_INEXISTENT_USER"), getLanguageManager().getString("Warnings", "INEXISTENT_USER")).build());
        } catch (Exception e) {
            reply(TypeEmbed.errorMessage(e, channel).build());
        }
    }

    public EmbedBuilder embed(HistoryMatch match, int ordem, int total) {
        EmbedBuilder embed = new EmbedBuilder();
        MatchPlayer matchPlayer = match.getMatchPlayer();

        Champion principalChampion = matchPlayer.getChampion(Language.Portuguese).get();

        embed.setAuthor(matchPlayer.getName(), null, principalChampion.getIcon());
        embed.setTitle("Historico de Partida");

        embed.setDescription(String.format(":trophy: Resultado: %s", winnerConvert(match.getWinner())))
                .appendDescription(String.format("\n:id: Id da Partida: `%s`", match.getMatchId()))
                .appendDescription(String.format("\n:game_die: Modo de Jogo: %s", match.getQueue().getName()))
                .appendDescription(String.format("\n:clock9: Duração: %s Minuto(s)", match.getMatchMinutes()));

        NumberFormat nf = NumberFormat.getInstance();

        embed.addField("Dano", nf.format(matchPlayer.getDamageRaw()), true);
        embed.addField("Dano Sofrido", nf.format(matchPlayer.getDamage().getDamageTaken()), true);
        embed.addField("KDA", matchPlayer.getKillsRaw() + "/" + matchPlayer.getDeaths(), true);

        embed.addField("Cura", nf.format(matchPlayer.getHealing()), true);
        embed.addField("Mapa", match.getMapGame(), true);
        embed.addBlankField(true);

        embed.setThumbnail(principalChampion.getIcon());
        embed.setImage("https://i.imgur.com/hnKkxGR.png");

        embed.setTimestamp(match.getMatchDate());
        embed.setFooter(String.format("[%s/%s] Partida ocorreu em ", ordem, total));
        embed.setColor(new Color(167, 255, 108));

        return embed;
    }

    public String winnerConvert(String string) {
        return (string.equalsIgnoreCase("Win")) ? "Vitória" : "Derrota";
    }

}
