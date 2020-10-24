package me.skiincraft.discord.herostats.listeners;

import me.skiincraft.api.paladins.common.EndPoint;
import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.objects.SearchPlayer;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ChampionChoiceObject {

    private final long userId;
    private final TextChannel channel;
    private final Message message;
    private final Champion champion;
    private final SearchPlayer searchPlayer;
    private final EndPoint requester;
    private final long time;

    public ChampionChoiceObject(long userId, TextChannel channel, Message message, Champion champion, SearchPlayer searchPlayer, EndPoint requester) {
        this.userId = userId;
        this.champion = champion;
        this.message = message;
        this.channel = channel;
        this.searchPlayer = searchPlayer;
        this.requester = requester;
        this.time = System.currentTimeMillis();
    }

    public Message getMessage() {
        return message;
    }

    public long getUserId() {
        return userId;
    }

    public Champion getChampion() {
        return champion;
    }

    public SearchPlayer getSearchPlayer() {
        return searchPlayer;
    }

    public TextChannel getChannel() {
        return channel;
    }

    public EndPoint getRequester() {
        return requester;
    }

    public long getTime() {
        return time;
    }
}
