package me.skiincraft.discord.herostats.commands;

import me.skiincraft.discord.core.command.Command;
import me.skiincraft.discord.herostats.HeroStatsBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", Arrays.asList("ajuda", "paladins"), "help");
    }

    @Override
    public void execute(User user, String[] args, TextChannel channel) {
        reply(embed(user).build());
    }

    public EmbedBuilder embed(User user){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Comandos Disponiveis: ");
        StringBuilder builder = new StringBuilder();
        HeroStatsBot.getMain().getPlugin().getCommandManager().getCommands().stream()
                .map(Command::getCommandName)
                .forEach(string ->{
                    builder.append("h!");
                    builder.append(string);
                    builder.append("\n");
                });
        embed.setThumbnail(user.getJDA().getSelfUser().getAvatarUrl());
        embed.setDescription(builder.toString());
        embed.setColor(new Color(255, 192,57));
        embed.setFooter("Bot em construção!");

        return embed;
    }

}
