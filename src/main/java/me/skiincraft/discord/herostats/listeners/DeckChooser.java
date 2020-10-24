package me.skiincraft.discord.herostats.listeners;

import me.skiincraft.api.paladins.entity.player.Loadout;
import me.skiincraft.discord.core.command.ChannelInteract;
import me.skiincraft.discord.core.command.ContentMessage;
import me.skiincraft.discord.core.utils.IntegerUtils;
import me.skiincraft.discord.herostats.commands.DeckCommand;
import me.skiincraft.discord.herostats.imagebuild.DeckPreviewImage;
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

public class DeckChooser extends ListenerAdapter {

    public static final List<DeckChooserObject> objects = new ArrayList<>();

    private void sendBiConsumer(long userId, Consumer<DeckChooserObject> consumer){
        DeckChooserObject chooser = objects.stream().filter(o -> o.getUserId() == userId).findAny().orElse(null);
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
        System.out.println(firstword);

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

            if (finalNum > chooser.getLoadoutSize()) {
                return;
            }

            chooser.getChooserMessage().editMessage(processing(chooser.getChooserMessage().getEmbeds().get(0)).build()).queue();

            ChannelInteract interact = new ChannelInteract() {
                @Override
                protected MessageChannel getTextChannel() {
                    return event.getChannel();
                }
            };
            objects.remove(chooser);

            Loadout loadout = chooser.getLoadouts().get(finalNum-1);
            InputStream input = DeckPreviewImage.drawImage(loadout);

            interact.reply(new ContentMessage(DeckCommand.embed(loadout).build(), input, "png"), message -> chooser.getChooserMessage().delete().queue());
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
}
