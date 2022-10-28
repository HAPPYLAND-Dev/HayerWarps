package me.xiaozhangup.hayerwarps.method;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import me.xiaozhangup.hayerwarps.HayerWarps;
import me.xiaozhangup.hayerwarps.Warps;
import me.xiaozhangup.hayerwarps.utils.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    public static List<Warps> warps = new ArrayList<>();
    public static int i = 0;

    public static void setup() {
        Bukkit.getScheduler().runTaskAsynchronously(HayerWarps.plugin, () -> {
            var config = ConfigManager.getConfig("warps");
            warps.clear();
            config.getKeys(false).forEach((s -> {
                var warp = new Warps(
                        config.getString(s + ".owner"),
                        config.getLocation(s + ".pos"),
                        Material.getMaterial(config.getString(s + ".icon")),
                        config.getString(s + ".name"),
                        s
                );
                warps.add(warp);
                //HayerWarps.plugin.getLogger().info(warp.getName() + " Loaded");
            }));});
    }

    public static void saveAll() {
        i = 0;
        warps.forEach(wp -> {
            ConfigManager.writeConfig(
                    "warps",
                    i + ".owner",
                    wp.getOwner()
            );
            ConfigManager.writeConfig(
                    "warps",
                    i + ".pos",
                    wp.getLocation()
            );
            ConfigManager.writeConfig(
                    "warps",
                    i + ".icon",
                    wp.getMaterial().toString()
            );
            ConfigManager.writeConfig(
                    "warps",
                    i + ".name",
                    wp.getName()
            );
            i++;
        });
    }

    public static void addWarp(Warps warp) {
        warps.add(warp);
        saveAll();
        setup();
    }

    public static void delete(Warps warps) {
        ConfigManager.writeConfig("warps", warps.getId() + ".icon", "BARRIER");
        setup();
    }

    public static boolean isOnLand(Player p) {
        if (landFromPlayer(p) == null) return false;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(p);
        return IridiumSkyblock.getInstance().getIslandManager().getPlayersOnIsland(landFromPlayer(p)).contains(user);
    }

    public static @Nullable Island landFromPlayer(Player p) {
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(p);
        var is = user.getIsland();
        return is.orElse(null);
    }

}
