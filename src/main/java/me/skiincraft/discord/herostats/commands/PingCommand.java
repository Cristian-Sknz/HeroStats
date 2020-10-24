package me.skiincraft.discord.herostats.commands;

import me.skiincraft.discord.core.utils.Emoji;
import me.skiincraft.discord.herostats.assets.PaladinsCommand;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class PingCommand extends PaladinsCommand {

    public PingCommand() {
        super("ping", null, "ping");
    }

    public void execute(User user, String[] args, TextChannel channel) {
        final long inicialms = System.currentTimeMillis();
        reply(message(user, channel).build(), m -> m.editMessage(message(user, channel).replace("{?}", (System.currentTimeMillis()-inicialms) + "").build()).queue());
    }

    public MessageBuilder message(User user, TextChannel channel) {
        MessageBuilder message = new MessageBuilder();
        message.append(user.getAsMention()).append(" Pong!");
        message.append("\n").append(Emoji.TIMER.getAsMention()).append("| GatewayPing: `").append(String.valueOf(channel.getJDA().getGatewayPing()));
        message.append("ms`\n").append(Emoji.INCOMING_ENVELOPE.getAsMention()).append("| API Ping: `{?}ms`");

        return message;
    }
}
