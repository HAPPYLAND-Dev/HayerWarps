package me.xiaozhangup.hayerwarps.method;

import me.xiaozhangup.hayerwarps.Warps;
import me.xiaozhangup.hayerwarps.method.Loader;
import me.xiaozhangup.hayerwarps.utils.manager.ConfigManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Like {

    public static List<Warps> getLiked(Player p) {
        List<Warps> ws = new ArrayList<>();
        ConfigManager.getConfig("liked").getIntegerList(p.getName()).forEach(integer -> {
            ws.add(Loader.warps.get(integer));
        });
        return ws;
    }

    public static void addLiked(Player p, Integer i) {
        var list = ConfigManager.getConfig("liked").getIntegerList(p.getName());
        list.add(i);
        ConfigManager.writeConfig(
                "liked",
                p.getName(),
                list
        );
    }

    public static void delLiked(Player p, Integer i) {
        var list = ConfigManager.getConfig("liked").getIntegerList(p.getName());
        list.remove(i);
        ConfigManager.writeConfig(
                "liked",
                p.getName(),
                list
        );
    }

}
