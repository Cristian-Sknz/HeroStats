package me.skiincraft.discord.herostats.choosers;

import me.skiincraft.api.paladins.common.EndPoint;
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
import me.skiincraft.discord.core.common.chooser.ChooserInterface;
import me.skiincraft.discord.core.common.chooser.ChooserObject;
import me.skiincraft.discord.core.configuration.LanguageManager;
import me.skiincraft.discord.core.utils.IntegerUtils;
import me.skiincraft.discord.herostats.commands.TypeEmbed;
import me.skiincraft.discord.herostats.imagebuild.ChampionImage;
import me.skiincraft.discord.herostats.utils.HeroUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChampionChooser extends ChannelInteract implements ChooserInterface {

    private final TextChannel channel;
    private final EndPoint endPoint;
    private final SearchPlayer searchPlayer;
    private final Message message;

    private Champion champion;

    public ChampionChooser(TextChannel channel, Message message, EndPoint endPoint, SearchPlayer searchPlayer) {
        this.channel = channel;
        this.endPoint = endPoint;
        this.message = message;
        this.searchPlayer = searchPlayer;
    }

    public ChampionChooser setChampion(Champion champion) {
        this.champion = champion;
        return this;
    }

    public Champion getChampion() {
        return champion;
    }

    @Override
    protected MessageChannel getTextChannel() {
        return channel;
    }

    @Override
    public boolean execute(String choice, Message message, Member member, ChooserObject object) {
        try {
            if (member.getIdLong() != object.getUserId()){
                return false;
            }
            LanguageManager lang = new LanguageManager(member.getGuild());
            if (object.getOptions()[0].equalsIgnoreCase(choice)) {
                channel.sendTyping().queue();
                PlayerChampions champions = endPoint.getPlayerChampions(searchPlayer.getUserId()).get();
                PlayerChampion champion = champions.getById(this.champion.getId());
                if (champion == null) {
                    reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_CHAMPION_NOT_LOCATED"), lang.getString("Warnings", "CHAMPION_NOT_LOCATED")).build());
                    this.message.delete().queue();
                    return true;
                }
                InputStream input = ChampionImage.drawImage(champion);
                reply(new ContentMessage(embed(champion, champions.getAsList(), searchPlayer, lang).build(), input, "png"));
                this.message.delete().queue();
                return true;
            }
            if (!IntegerUtils.isNumeric(choice)) {
                return false;
            }

            Queue queue = getQueueByNumber(Integer.parseInt(choice), Platform.getPlatformByPortalId(searchPlayer.getPortalId()) != Platform.PC);
            if (queue == null) {
                return false;
            }

            channel.sendTyping().queue();
            QueueChampions champions = endPoint.getQueueStats(searchPlayer.getUserId(), queue).get();
            QueueChampion champion = champions.getById(this.champion.getId());

            if (champion == null) {
                reply(TypeEmbed.simpleEmbed(lang.getString("Warnings", "T_CHAMPION_NOT_LOCATED"), lang.getString("Warnings", "CHAMPION_NOT_LOCATED")).build());
                this.message.delete().queue();
                return true;
            }
            InputStream input = ChampionImage.drawImage(champion);
            reply(new ContentMessage(embed(champion, champions.getAsList(), searchPlayer, lang).build(), input, "png"));
            this.message.delete().queue();
        } catch (Exception e){
            reply(TypeEmbed.errorMessage(e, channel).build());
            this.message.delete().queue();
            return true;
        }
        return false;
    }

    public EmbedBuilder embed(QueueChampion rank, java.util.List<QueueChampion> lista, SearchPlayer searchPlayer, LanguageManager lang) {
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
