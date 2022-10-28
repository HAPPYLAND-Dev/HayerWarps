package me.xiaozhangup.hayerwarps;

import me.xiaozhangup.hayerwarps.holder.HAllWarps;
import me.xiaozhangup.hayerwarps.holder.HYourWarps;
import me.xiaozhangup.hayerwarps.method.Loader;
import me.xiaozhangup.hayerwarps.utils.items.IBuilder;
import me.xiaozhangup.hayerwarps.utils.tools.IString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class YourWarps implements Listener {

    public static List<Integer> wslot = Arrays.asList(
            10,11,12,13,14,15,16
    );
    public static final ItemStack bg = IBuilder.getBorder(Material.GRAY_STAINED_GLASS_PANE);
    public static HashMap<Player, List<Warps>> ch = new HashMap<>();
    public static final ItemStack back = IBuilder.buildItem(Material.COMPASS, "&c返回上一页");


    public static void open(Player p) {
        p.sendTitle(IString.addColor("&c正在读取数据文件"), IString.addColor("&f请稍后"));
        Bukkit.getScheduler().runTaskAsynchronously(HayerWarps.plugin, () -> {
            List<Warps> own = new ArrayList<>();
            String name = p.getName();
            Loader.warps.forEach(warps -> {
                if (name.equals(warps.getOwner())) {
                    own.add(warps);
                }
            });
            ch.put(p, own);

            Inventory inv = Bukkit.createInventory(new HYourWarps(), 27, "你的岛屿传送点");

            for (int i = 0; i < 27; i++) {
                inv.setItem(i, bg);
            }

            int di = 0;
            for (Integer i : wslot) {
                if (own.size() <= di) break;
                if (own.get(di).material == Material.BARRIER) {
                    inv.setItem(
                            i,
                            IBuilder.buildItem(
                                    Material.BARRIER,
                                    "&c已删除的传送点"
                            )
                    );
                } else {
                    inv.setItem(
                            i,
                            IBuilder.buildItem(
                                    own.get(di).material,
                                    "&f" + own.get(di).getName(),
                                    "",
                                    "&e左键 &7- 传送到对应位置",
                                    "&e右键 &7- &c标记为删除 &7(不可逆)"
                            )
                    );
                }
                di++;
            }
            inv.setItem(26, back);

            Bukkit.getScheduler().runTask(HayerWarps.plugin, () -> {
                p.openInventory(inv);
                p.sendTitle(IString.addColor(""), IString.addColor(""));
            });

        });
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player p && e.getInventory().getHolder() instanceof HYourWarps) {
            e.setCancelled(true);
            int slot = e.getRawSlot();
            if (slot == 26) {
                AllWarps.open(p, 1);
            }
            if (slot <= 16 && slot >= 10) {
                if (e.getCurrentItem().getType() == Material.BARRIER) return;
                switch (e.getClick()) {
                    case RIGHT -> {
                        if (ch.get(p).size() <= slot - 10) return;
                        Loader.delete(ch.get(p).get(slot - 10));
                        p.closeInventory();
                        HayerWarps.send(p, "&c已删除这个传送点!");
                    }
                    case LEFT -> {
                        if (ch.get(p).size() <= slot - 10) return;
                        Warps warps = ch.get(p).get(slot - 10);
                        p.teleport(warps.location);
                        HayerWarps.send(p, "&7已将您传送到 &f" + warps.name);
                    }
                }
            }
        }
    }

}
