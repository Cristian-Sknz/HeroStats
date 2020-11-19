package me.skiincraft.discord.herostats.commands;

import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.command.Command;
import me.skiincraft.discord.core.command.InteractChannel;
import me.skiincraft.discord.herostats.HeroStatsBot;
import me.skiincraft.discord.herostats.assets.Category;
import me.skiincraft.discord.herostats.assets.PaladinsCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.SelfUser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", Arrays.asList("ajuda", "paladins"), "help");
    }

    public Category category() {
        return Category.Owner;
    }

    @Override
    public void execute(Member user, String[] args, InteractChannel channel) {
        channel.reply(helpEmbed().build());
    }

    public EmbedBuilder helpEmbed(){
        EmbedBuilder embed = new EmbedBuilder();
        SelfUser self = HeroStatsBot.getMain().getShardManager().getShards().get(0).getSelfUser();
        embed.setAuthor(self.getName(), null, self.getAvatarUrl());
        embed.setTitle(":tickets: Lista de Comandos");
        Category[] categories = new Category[] {Category.Statistics, Category.Match, Category.Ranking};
        List<PaladinsCommand> commands = OusuCore.getCommandManager().getCommands()
                .stream().filter(command -> command instanceof PaladinsCommand)
                .map(command -> (PaladinsCommand) command)
                .collect(Collectors.toList());
        for (Category category: categories){
            String[] categoryCommand = commands.stream().filter(command -> command.category() == category)
                    .map(PaladinsCommand::getCommandName).toArray(String[]::new);

            embed.addField(category.getName(), String.join("\n", categoryCommand), true);
        }
        embed.setThumbnail(self.getAvatarUrl());
        embed.setFooter("HeroStats está em versão beta, pode ocorrer bugs.");
        return embed;
    }

}
