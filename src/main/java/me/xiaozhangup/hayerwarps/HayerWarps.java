package me.xiaozhangup.hayerwarps;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.bank.BankItem;
import me.xiaozhangup.hayerwarps.method.Loader;
import me.xiaozhangup.hayerwarps.utils.command.Command;
import me.xiaozhangup.hayerwarps.utils.manager.ConfigManager;
import me.xiaozhangup.hayerwarps.utils.manager.ListenerManager;
import me.xiaozhangup.hayerwarps.utils.tools.IString;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class HayerWarps extends JavaPlugin {

    public static Plugin plugin;
    public static ListenerManager listenerManager = new ListenerManager();
    private static Economy econ = null;

    public static Economy getEconomy() {
        return econ;
    }
    @Override
    public void onEnable() {
        plugin = this;
        setupEconomy();

        saveDefaultConfig();
        reloadConfig();
        ConfigManager.createFile("warps");
        ConfigManager.createFile("liked");

        listenerManager.addListeners(
                new SignChoose(), new AllWarps()
        );
        listenerManager.register();

//转换器
//        var config = ConfigManager.getConfig("data");
//        config.getKeys(false).forEach(s -> {
//            Warps warps = new Warps(
//                    "xiaozhangup",
//                    config.getLocation(s + ".loc"),
//                    Material.getMaterial(config.getString(s+ ".item")),
//                    s
//            );
//            Loader.addWarp(warps);
//        });

        Command.register("pw", (commandSender, command, s, inside) -> {
            if (inside.length == 0) {
                AllWarps.open((Player) commandSender, 1);
            } else {
                Player p = (Player) commandSender;
                send(p, "&b岛屿传送点帮助\n");
                send(p, "&e每创建一个公共岛屿传送点需要100岛屿水晶");
                send(p, "&f/pwadd [传送点名] &7- 创建一个传送点");
                send(p, "&f/pwliked &7- 打开你收藏的传送点");
                send(p, "&f/pw &7- 打开传送点菜单\n");
            }
            return false;
        });
        Command.register("pwadd", (commandSender, command, s, inside) -> {
            Player p = (Player) commandSender;
            if (inside.length == 0) {
                send(p, "&c你必须指定一个名字! &7(例如/pwadd 我的传送点)");
            } else {
                if (Loader.isOnLand(p)) {
                    var island = Loader.landFromPlayer(p);
                    BankItem crystal = IridiumSkyblock.getInstance().getBankItems().crystalsBankItem;
                    var bank = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island, crystal);
                    if (bank.getNumber() < 100) {
                        send(p, "&f创建传送点需要 100 岛屿水晶!");
                    } else {
                        SignChoose.open(p, inside[0]);
                        bank.setNumber(bank.getNumber() - 100);
                    }
                } else {
                    send(p, "&c你只能在你的岛屿创建传送点!");
                }
            }
            return false;
        });
        Command.register("pwliked", (commandSender, command, s, inside) -> {
            /*your way*/
            return false;
        });

        Loader.setup();

    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            econ = economyProvider.getProvider();
        }
    }

    public static void send(Player p, String message) {
        p.sendMessage(IString.addColor("&8[&x&0&9&C&6&F&9空岛&8] " + message));
    }

}
