package me.skiincraft.discord.herostats.commands;

import me.skiincraft.discord.core.command.InteractChannel;
import me.skiincraft.discord.herostats.assets.Category;
import me.skiincraft.discord.herostats.assets.PaladinsCommand;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class PingCommand extends PaladinsCommand {

    public PingCommand() {
        super("ping", null, "ping");
    }

    public Category category() {
        return Category.Owner;
    }

    public void execute(Member user, String[] args, InteractChannel channel) {
        final long ping = System.currentTimeMillis();
        channel.reply(message(user, channel.getTextChannel()).build(), m -> m.editMessage(message(user, channel.getTextChannel()).replace("{?}", (System.currentTimeMillis()-ping) + "").build()).queue());
    }

    public MessageBuilder message(Member user, TextChannel channel) {
        MessageBuilder message = new MessageBuilder();
        message.append(user.getAsMention()).append(" Pong!");
        message.append("\n:timer:").append("| GatewayPing: `").append(String.valueOf(channel.getJDA().getGatewayPing()));
        message.append("ms`\n:incoming_envelope:").append("| API Ping: `{?}ms`");

        return message;
    }
}
