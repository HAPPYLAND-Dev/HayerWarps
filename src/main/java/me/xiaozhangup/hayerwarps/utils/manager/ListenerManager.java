package me.xiaozhangup.hayerwarps.utils.manager;

import me.xiaozhangup.hayerwarps.HayerWarps;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ListenerManager {

    private final ArrayList<Listener> listeners = new ArrayList<>();


    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void addListeners(Listener... listener) {
        listeners.addAll(Arrays.asList(listener));
    }

    public void addListeners(Collection<? extends Listener> listener) {
        listeners.addAll(listener);
    }

    public void register() {
        listeners.forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, HayerWarps.plugin));
        listeners.clear();
    }

}
