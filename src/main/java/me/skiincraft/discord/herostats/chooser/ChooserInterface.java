package me.skiincraft.discord.herostats.chooser;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

@FunctionalInterface
public interface ChooserInterface {
    boolean execute(String choice, Message message, Member member, ChooserObject object);
}
