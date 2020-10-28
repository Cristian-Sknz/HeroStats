package me.skiincraft.discord.herostats.commands;

import me.skiincraft.api.paladins.Paladins;
import me.skiincraft.api.paladins.hirez.DataUsed;
import me.skiincraft.discord.herostats.assets.Category;
import me.skiincraft.discord.herostats.assets.PaladinsCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.Arrays;

public class DataUsedCommand extends PaladinsCommand {

    public DataUsedCommand() {
        super("dataused", Arrays.asList("data", "used"), "dataused");
    }

    public Category category() {
        return Category.Owner;
    }

    @Override
    public void execute(User user, String[] args, TextChannel channel) {
        if (!user.getId().equalsIgnoreCase("247096601242238991")){
            reply("Você não tem permissão para isso.");
            return;
        }
        try {
            DataUsed dataused = Paladins.getInstance().getDataUsed(endpoint().getSession()).get();
            reply(datausedEmbed(dataused).build());
        } catch (Exception e){
            reply(TypeEmbed.errorMessage(e, channel).build());
        }
    }

    public EmbedBuilder datausedEmbed(DataUsed dataUsed){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":small_red_triangle: Dados Usados");
        embed.setDescription("Estes são os dados utilizados na API atualmente.");
        embed.setThumbnail("https://web2.hirez.com/paladins/assets/paladins-logo.png");

        embed.addField("Requests:", dataUsed.getTotalRequestToday() + "/7500", true);
        embed.addField("Sessões ativas:", dataUsed.getActiveSessions() + "/50", true);
        embed.addField("Sessões totais:", dataUsed.getTotalSessionsToday() + "/500", true);

        embed.setFooter("' Essas são as informações totais de hoje. '", "https://www.entegral.net/wp-content/uploads/2016/11/custom-api-300x215.png");
        embed.setColor(new Color(18, 139, 162));

        return embed;
    }


}
