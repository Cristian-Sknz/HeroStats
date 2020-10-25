package me.skiincraft.discord.herostats.chooser;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class ChooserListeners extends ListenerAdapter {

    private final List<ChooserObject> chooserObjects;
    private final Map<ChooserObject, ChooserInterface> chooserInterfaces;

    public ChooserListeners() {
        this.chooserObjects = new ArrayList<>();
        this.chooserInterfaces = new HashMap<>();
    }

    public boolean contains(ChooserObject chooserObject){
        return chooserObjects.contains(chooserObject) && chooserInterfaces.containsKey(chooserObject);
    }

    public void addElement(ChooserObject object, ChooserInterface chooserInterface){
        if (chooserObjects.contains(object) || chooserInterfaces.containsKey(object)){
            System.err.println("Você tentou registrar um Chooser que existente.");
            return;
        }
        chooserObjects.add(object);
        chooserInterfaces.put(object, chooserInterface);
    }

    public void removeElement(ChooserObject object){
        chooserObjects.remove(object);
        chooserInterfaces.remove(object);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.isWebhookMessage() || Objects.requireNonNull(event.getMember()).getUser().isBot()) {
            return;
        }

        List<ChooserObject> expireds = chooserObjects.stream()
                .filter(ChooserObject::hasExpire)
                .collect(Collectors.toList());

        for (ChooserObject expired : expireds) {
            removeElement(expired);
        }

        List<ChooserObject> choosers = chooserObjects.stream()
                .filter(object -> object.getGuildId() == event.getGuild().getIdLong())
                .filter(object -> object.getChannelId() == event.getChannel().getIdLong())
                .collect(Collectors.toList());

        if (choosers.size() == 0) {
            return;
        }

        choosers.forEach(chooser -> {
            if (chooserInterfaces.containsKey(chooser)) {
                ChooserInterface chooserInterface = chooserInterfaces.get(chooser);
                boolean response = chooserInterface.execute(event.getMessage().getContentRaw(), event.getMessage(), event.getMember(), chooser);
                if (response){
                    removeElement(chooser);
                }
            }
        });
    }
}
