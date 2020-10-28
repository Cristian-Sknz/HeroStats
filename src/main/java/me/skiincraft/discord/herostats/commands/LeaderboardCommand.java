package me.skiincraft.discord.herostats.commands;

import me.skiincraft.api.paladins.entity.leaderboard.LeaderBoard;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.enums.Tier;
import me.skiincraft.api.paladins.exceptions.SearchException;
import me.skiincraft.api.paladins.objects.Place;
import me.skiincraft.discord.core.common.reactions.ReactionObject;
import me.skiincraft.discord.core.common.reactions.Reactions;
import me.skiincraft.discord.core.common.reactions.custom.ReactionPage;
import me.skiincraft.discord.core.utils.StringUtils;
import me.skiincraft.discord.herostats.assets.Category;
import me.skiincraft.discord.herostats.assets.PaladinsCommand;
import me.skiincraft.discord.herostats.utils.HeroUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.collections4.ListUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LeaderboardCommand extends PaladinsCommand {

    public LeaderboardCommand() {
        super("leaderboard", null, "leaderboard <elo>");
    }

    public Category category() {
        return Category.Ranking;
    }

    @Override
    public void execute(User user, String[] args, TextChannel channel) {
        if (args.length == 0){
            reply("h!" + getUsage());
            return;
        }

        String elo = replaceNumbers(String.join("_", args));
        Tier tier = Arrays.stream(Tier.values()).filter(t -> valid(t, elo))
                .min((t1, t2) -> Integer.compare(t2.getRankId(), t1.getRankId()))
                .orElse(Arrays.stream(Tier.values())
                        .filter(t -> StringUtils.containsEqualsIgnoreCase(t.name(), elo))
                        .min((t1, t2) -> Integer.compare(t2.getRankId(), t1.getRankId()))
                        .orElse(null));

        if (tier == null){
            reply(TypeEmbed.WarningEmbed("Elo não encontrado!", "Esse elo que você digitou é invalido").build());
            return;
        }

        boolean isGrandMaster = false;

        if (tier == Tier.Grandmaster){
            isGrandMaster = true;
            tier = Tier.Master;
        }

        try {
            LeaderBoard leaderboard = leaderBoard(tier);
            List<Place> places = leaderboard.getAsList();

            if (places.size() == 0) {
                reply("Não foi possivel responder a sua solicitação, API não retornou nenhum valor");
                return;
            }

            if (isGrandMaster && places.get(0).getPoints() < 100) {
                reply(grandMasterNull().build());
                return;
            }

            if (isGrandMaster && places.get(0).getPoints() > 100) {
                places.removeAll(places.stream().filter(place -> place.getPoints() < 100).collect(Collectors.toList()));
            }

            List<List<Place>> partition = ListUtils.partition(places, 10);
            List<EmbedBuilder> embeds = new ArrayList<>();

            boolean isMaster = tier == Tier.Master && places.get(0).getPoints() > 99;
            int size = partition.size();
            if (isMaster) {
                if (isGrandMaster) {
                    tier = Tier.Grandmaster;
                    size = 10;
                } else {
                    places.removeAll(places.stream().filter(place -> place.getPoints() >= 100)
                            .limit(100).collect(Collectors.toList()));

                    partition = ListUtils.partition(places, 10);
                    size = partition.size();
                }
            }

            for (int i = 1; i <= size; i++) {
                if (isGrandMaster && i == 19) break;
                List<Place> value = partition.get(i - 1);
                embeds.add(places(value, tier, i * 10, size * 10));
            }

            reply(embeds.get(0).build(), message -> Objects.requireNonNull(Reactions.getInstance()).registerReaction(new ReactionObject(message, user.getIdLong(), new String[]{"U+25C0", "U+25B6"}),
                    new ReactionPage(embeds, true)));
        } catch (SearchException e){
            reply(unavailableTier(tier).build());
        } catch (Exception e) {
            reply(TypeEmbed.errorMessage(e, channel).build());
        }
    }

    public EmbedBuilder unavailableTier(Tier tier){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(tier.getName(Language.Portuguese), null, HeroUtils.getTierImage(tier));
        embed.setTitle("Tabela Indisponível");
        embed.setDescription("Essa tabela de classificação está indisponível\n" +
                "o motivo pode ser por ninguém estar nesse elo ainda.");
        embed.setColor(new Color(255, 214,0));
        embed.setThumbnail(HeroUtils.getTierImage(tier));
        embed.setFooter("Tabela de Classificação");

        return embed;
    }

    public EmbedBuilder grandMasterNull() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor("Paladins Leaderboard", null, HeroUtils.getTierImage(Tier.Grandmaster));
        embed.setTitle("Tabela de Classificação");
        embed.setDescription("Ninguem chegou no Grão Mestre ainda!");

        embed.setThumbnail(HeroUtils.getTierImage(Tier.Grandmaster));

        embed.setColor(new Color(47, 196, 89));
        embed.setFooter("[1/1]");

        return embed;
    }

    public EmbedBuilder places(List<Place> place, Tier tier, int page, int maxpages){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor("Paladins Leaderboard", null, HeroUtils.getTierImage(tier));
        embed.setTitle("Tabela de Classificação");
        embed.setDescription("Você está vendo a classificação do elo: " + tier.getName(Language.Portuguese));

        embed.setThumbnail(HeroUtils.getTierImage(tier));

        StringBuilder points = new StringBuilder();
        StringBuilder names = new StringBuilder();
        StringBuilder wins = new StringBuilder();

        for (Place pos : place){
            points.append(pos.getPoints()).append("\n");
            names.append((pos.getUsername().length() < 2) ? "[privado]":pos.getUsername()).append("\n");
            wins.append(pos.getWins())
                    .append("/")
                    .append(pos.getLosses())
                    .append("\n");
        }
        embed.addField("Pontos:", points.toString(), true);
        embed.addField("Jogador:", names.toString(), true);
        embed.addField("Win/Loss:", wins.toString(), true);

        embed.setColor(new Color(47, 196, 89));
        embed.setFooter("[" + page + "/" + maxpages + "]");

        return embed;
    }


    private String replaceNumbers(String args) {
        return args
                .replace("1", "I")
                .replace("2", "II")
                .replace("3", "III")
                .replace("4", "IV")
                .replace("5", "V")
                .replace("Grão Mestre", "Grandmaster")
                .replace("GM", "Grandmaster");
    }

    private boolean valid(Tier tier, String string){
        return StringUtils.containsEqualsIgnoreCase(tier.name()
                .replace("Platinum", "Platina")
                .replace("Diamond", "Diamante")
                .replace("Silver", "Prata")
                .replace("Gold", "Ouro")
                .replace("Master", "Mestre"), string);
    }
}

