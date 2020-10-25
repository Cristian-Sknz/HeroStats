package me.skiincraft.discord.herostats.chooser;

import net.dv8tion.jda.api.entities.TextChannel;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class ChooserObject {

    private final long guildId;
    private final long userId;
    private final long channelId;

    private final String[] options;

    private final long createdTime;
    private long durationTime;

    public ChooserObject(long userId, TextChannel channel, String[] option) {
        this.guildId = channel.getGuild().getIdLong();
        this.channelId = channel.getIdLong();
        this.userId = userId;
        this.options = option;
        this.createdTime = System.currentTimeMillis();
        this.durationTime = 13000L; // 13s
    }


    public ChooserObject setDurationTime(Duration duration){
        durationTime = TimeUnit.SECONDS.toMillis(duration.getSeconds());
        return this;
    }

    public ChooserObject setDurationTime(long time, TimeUnit unit){
        durationTime = unit.toMillis(time);
        return this;
    }

    public boolean hasExpire(){
        return durationTime != 0 && (System.currentTimeMillis() - createdTime) >= durationTime;
    }

    public long getUserId() {
        return userId;
    }

    public long getGuildId() {
        return guildId;
    }

    public long getChannelId() {
        return channelId;
    }

    public String[] getOptions() {
        return options;
    }
}
