package me.skiincraft.discord.herostats.listeners;

import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.player.PlayerChampion;
import me.skiincraft.api.paladins.entity.player.QueueChampion;
import me.skiincraft.api.paladins.entity.player.objects.PlayerChampions;
import me.skiincraft.api.paladins.entity.player.objects.QueueChampions;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.enums.Queue;
import me.skiincraft.api.paladins.objects.SearchPlayer;
import me.skiincraft.discord.core.command.ChannelInteract;
import me.skiincraft.discord.core.command.ContentMessage;
import me.skiincraft.discord.core.configuration.LanguageManager;
import me.skiincraft.discord.core.utils.IntegerUtils;
import me.skiincraft.discord.herostats.commands.TypeEmbed;
import me.skiincraft.discord.herostats.imagebuild.ChampionImage;
import me.skiincraft.discord.herostats.utils.HeroUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ChampionChooser extends ListenerAdapter {

    public static final List<ChampionChoiceObject> objects = new ArrayList<>();

    private void sendBiConsumer(long userId, Consumer<ChampionChoiceObject> consumer){
        ChampionChoiceObject chooser = objects.stream().filter(o -> o.getUserId() == userId).findAny().orElse(null);
        if (chooser == null) return;
        consumer.accept(chooser);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()){
            return;
        }
        if (objects.size() == 0){
            return;
        }

        if(!event.getChannel().canTalk()){
            return;
        }

        objects.forEach(o -> {
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - o.getTime()) > 90){
                objects.remove(o);
            }
        });

        String firstword = event.getMessage().getContentRaw().split(" ")[0];
        if (!IntegerUtils.isNumeric(firstword)){
            return;
        }

        if (firstword.length() >= 2){
            return;
        }

        int num = Integer.parseInt(firstword);
        if (num == 0) num++;
        final int finalNum = num;
        sendBiConsumer(Objects.requireNonNull(event.getMember()).getIdLong(), (chooser) -> {
            if (event.getChannel().getIdLong() != chooser.getChannel().getIdLong()){
                return;
            }

            if (finalNum > 5) {
                return;
            }

            chooser.getMessage().editMessage(processing(chooser.getMessage().getEmbeds().get(0)).build()).queue();

            ChannelInteract interact = new ChannelInteract() {
                @Override
                protected MessageChannel getTextChannel() {
                    return event.getChannel();
                }
            };
            LanguageManager lang = new LanguageManager(event.getGuild());
            objects.remove(chooser);
            try {
            if (finalNum == 1){
                PlayerChampions champions = chooser.getRequester().getPlayerChampions(chooser.getSearchPlayer().getUserId()).get();
                PlayerChampion champion = champions.getById(chooser.getChampion().getId());
                if (champion == null){
                    interact.reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_CHAMPION_NOT_LOCATED"), lang.getString("Warnings", "CHAMPION_NOT_LOCATED")).build());
                    chooser.getMessage().delete().queue();
                    return;
                }
                InputStream input = ChampionImage.drawImage(champion);
                interact.reply(new ContentMessage(embed(champion, champions.getAsList(), chooser.getSearchPlayer(), lang).build(), input, "png"));
                chooser.getMessage().delete().queue();
            }

            Queue queue = getQueueByNumber(finalNum, Platform.getPlatformByPortalId(chooser.getSearchPlayer().getPortalId()) != Platform.PC);
            if (queue == null) {
                return;
            }

            QueueChampions champions = chooser.getRequester().getQueueStats(chooser.getSearchPlayer().getUserId(), queue).get();
            QueueChampion champion = champions.getById(chooser.getChampion().getId());

            if (champion == null){
                interact.reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_CHAMPION_NOT_LOCATED"), lang.getString("Warnings", "CHAMPION_NOT_LOCATED")).build());
                chooser.getMessage().delete().queue();
                return;
            }
            InputStream input = ChampionImage.drawImage(champion);
            interact.reply(new ContentMessage(embed(champion, champions.getAsList(), chooser.getSearchPlayer(), lang).build(), input, "png"));
            chooser.getMessage().delete().queue();
            } catch (Exception e){
                interact.reply(TypeEmbed.errorMessage(e, chooser.getChannel()).build());
            }
        });
    }

    public EmbedBuilder processing(MessageEmbed embedOriginal){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Processando!");
        MessageEmbed.AuthorInfo authorInfo = embedOriginal.getAuthor();

        embed.setAuthor(Objects.requireNonNull(authorInfo).getName(), authorInfo.getUrl(), authorInfo.getIconUrl());
        embed.setThumbnail(Objects.requireNonNull(embedOriginal.getThumbnail()).getUrl());
        embed.setDescription(embedOriginal.getDescription());
        embed.appendDescription("\nAguarde um momento.");
        embed.setColor(Color.GREEN);
        return embed;
    }

    public EmbedBuilder embed(QueueChampion rank, List<QueueChampion> lista, SearchPlayer searchPlayer, LanguageManager lang) {
        EmbedBuilder embed = new EmbedBuilder();
        int place = 1 + lista.indexOf(rank);

        Champion champion = rank.getChampion(Language.Portuguese).get();
        embed.setAuthor(searchPlayer.getInGameName(), null, champion.getIcon());
        embed.setTitle(lang.getString(this.getClass(), "EMBEDTITLE"));
        embed.setThumbnail(champion.getIcon());

        String placemessage = (place == 1)
                ? lang.getString(this.getClass(), "BESTCHAMPION").replace("{CHAMPION}", rank.getChampionName())
                .replace("{PLAYER}", searchPlayer.getInGameName())
                : lang.getString(this.getClass(), "PLACECHAMPION").replace("{CHAMPION}", rank.getChampionName())
                .replace("{PLACE}", place + "º").replace("{PLAYER}", searchPlayer.getInGameName());

        String playedtime = (TimeUnit.MILLISECONDS.toMinutes(rank.getMillisPlayed()) / 60 != 0) ? TimeUnit.MILLISECONDS.toMinutes(rank.getMillisPlayed()) / 60 + " hora(s)"
                : TimeUnit.MILLISECONDS.toMinutes(rank.getMillisPlayed()) + " minuto(s)";

        embed.setDescription("<:cards:728729369756958750> " + placemessage);
        embed.appendDescription("\n:clock3: " + lang.getString(this.getClass(), "TIMEPLAYED") + playedtime);
        embed.setColor(HeroUtils.paladinsClassColor(champion));

        embed.setFooter("Jogou pela ultima vez em");
        embed.setTimestamp(rank.getLastPlayed());

        embed.addField("Modo", rank.getQueue().getName(), true);
        embed.addField("Taxa de Vitoria", IntegerUtils.getPorcentagem(rank.getWins() + rank.getLosses(), rank.getWins()), true);
        embed.addField("Taxa de Abates", IntegerUtils.getPorcentagem(rank.getKills() + rank.getDeaths(), rank.getKills()), true);

        return embed;
    }

    public EmbedBuilder embed(PlayerChampion rank, List<PlayerChampion> lista, SearchPlayer searchPlayer, LanguageManager lang) {
        EmbedBuilder embed = new EmbedBuilder();
        int place = 1 + lista.indexOf(rank);

        Champion champion = rank.getChampion(Language.Portuguese).get();
        embed.setAuthor(searchPlayer.getInGameName(), null, champion.getIcon());
        embed.setTitle(lang.getString(this.getClass(), "EMBEDTITLE"));
        embed.setThumbnail(champion.getIcon());

        String placemessage = (place == 1)
                ? lang.getString(this.getClass(), "BESTCHAMPION").replace("{CHAMPION}", rank.getChampionName())
                .replace("{PLAYER}", searchPlayer.getInGameName())
                : lang.getString(this.getClass(), "PLACECHAMPION").replace("{CHAMPION}", rank.getChampionName())
                .replace("{PLACE}", place + "º").replace("{PLAYER}", searchPlayer.getInGameName());

        String playedtime = (TimeUnit.MILLISECONDS.toMinutes(rank.getMillisPlayed()) / 60 != 0) ? TimeUnit.MILLISECONDS.toMinutes(rank.getMillisPlayed()) / 60 + " hora(s)"
                : TimeUnit.MILLISECONDS.toMinutes(rank.getMillisPlayed()) + " minuto(s)";

        embed.setDescription("<:cards:728729369756958750> " + placemessage);
        embed.appendDescription("\n:clock3: " + lang.getString(this.getClass(), "TIMEPLAYED") + playedtime);
        embed.setColor(HeroUtils.paladinsClassColor(champion));

        embed.setFooter("Jogou pela ultima vez em");
        embed.setTimestamp(rank.getLastPlayed());

        embed.addField("Modo", "Todos os modos.", true);
        embed.addField("Taxa de Vitoria", IntegerUtils.getPorcentagem(rank.getWins() + rank.getLosses(), rank.getWins()), true);
        embed.addField("Taxa de Abates", IntegerUtils.getPorcentagem(rank.getKills() + rank.getDeaths(), rank.getKills()), true);

        return embed;
    }

    private Queue getQueueByNumber(int number, boolean isConsole) {
        if (number == 2) return (isConsole)? Queue.Live_Competitive_GamePad : Queue.Live_Competitive_Keyboard;
        if (number == 3) return Queue.Live_Siege;
        if (number == 4) return Queue.Live_Team_DeathMatch;
        if (number == 5) return Queue.Live_Onslaught;
        else return null;
    }
}
