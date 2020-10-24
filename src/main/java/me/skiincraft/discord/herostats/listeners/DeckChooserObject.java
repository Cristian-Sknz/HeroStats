package me.skiincraft.discord.herostats.listeners;

import me.skiincraft.api.paladins.entity.player.Loadout;
import me.skiincraft.api.paladins.entity.player.objects.Loadouts;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class DeckChooserObject {

    private final long userId;
    private final TextChannel channel;
    private final List<Loadout> loadouts;
    private final Message chooserMessage;
    private final int loadoutsize;

    private final long time;

    public DeckChooserObject(long userId, TextChannel channel, List<Loadout> loadouts, Message chooserMessage) {
        this.userId = userId;
        this.channel = channel;
        this.loadouts = loadouts;
        this.loadoutsize = loadouts.size();
        this.chooserMessage = chooserMessage;
        this.time = System.currentTimeMillis();
    }

    public Message getChooserMessage() {
        return chooserMessage;
    }

    public List<Loadout> getLoadouts() {
        return loadouts;
    }

    public int getLoadoutSize() {
        return loadoutsize;
    }

    public long getUserId() {
        return userId;
    }

    public long getTime() {
        return time;
    }

    public TextChannel getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return "DeckChooserObject{" +
                "userId=" + userId +
                ", channel=" + channel +
                ", loadouts=" + loadouts +
                ", chooserMessage=" + chooserMessage +
                ", loadoutsize=" + loadoutsize +
                ", time=" + time +
                '}';
    }
}
