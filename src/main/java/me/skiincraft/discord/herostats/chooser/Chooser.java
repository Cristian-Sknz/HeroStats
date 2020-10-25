package me.skiincraft.discord.herostats.chooser;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Chooser {

    private static final List<Chooser> allChooser = new ArrayList<>();
    private final ChooserListeners listener;

    public Chooser(ChooserListeners listener){
        this.listener = listener;
        if (allChooser.stream().noneMatch(chooser -> chooser.getListener() == listener)){
            allChooser.add(this);
        }
    }

    public void registerChooser(ChooserObject object, ChooserInterface chooser){
        listener.addElement(object, chooser);
    }

    public void removeChooser(ChooserObject object){
        listener.removeElement(object);
    }

    public static Chooser of(ChooserListeners listener){
        return allChooser.stream().filter(chooser -> chooser.getListener() == listener).findFirst().orElse(new Chooser(listener));
    }

    public static List<Chooser> of(ChooserObject object){
        return allChooser.stream().filter(chooser -> chooser.getListener().contains(object)).collect(Collectors.toList());
    }

    @Nullable
    public static Chooser getInstance() {
        if (allChooser.size() == 0){
            return null;
        }
        return allChooser.get(0);
    }

    public ChooserListeners getListener() {
        return listener;
    }


}
