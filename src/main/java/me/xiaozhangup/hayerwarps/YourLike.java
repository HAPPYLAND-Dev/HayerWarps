package me.xiaozhangup.hayerwarps;

import me.xiaozhangup.hayerwarps.holder.HAllWarps;
import me.xiaozhangup.hayerwarps.holder.HYourLike;
import me.xiaozhangup.hayerwarps.holder.HYourWarps;
import me.xiaozhangup.hayerwarps.method.Like;
import me.xiaozhangup.hayerwarps.method.Loader;
import me.xiaozhangup.hayerwarps.utils.items.IBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

import static me.xiaozhangup.hayerwarps.AllWarps.rawToID;

public class YourLike implements Listener {
    public static List<Integer> wslot = Arrays.asList(
            10,11,12,13,14,
            19,20,21,22,23,
            28,29,30,31,32
    );
    public static final ItemStack bg = IBuilder.getBorder(Material.GRAY_STAINED_GLASS_PANE);

    public static final ItemStack back = IBuilder.buildItem(Material.COMPASS, "&c返回上一页");

    public static void open(Player p) {
        Inventory inv = Bukkit.createInventory(new HYourLike(), 45, "你喜欢的传送点");

        for (int i = 0; i < 45; i++) {
            if (wslot.contains(i)) continue;
            inv.setItem(i, bg);
        }

        int ws = 0;
        for (Integer i : wslot) {
            if (Like.getLiked(p).size() <= ws) continue;
            var warp = Like.getLiked(p).get(ws);
            if (warp.getMaterial() == Material.BARRIER) {
                inv.setItem(i,
                        IBuilder.buildItem(
                                warp.material,
                                "&c已删除的传送点",
                                "",
                                "&e单击 &7- &c取消收藏"
                        )
                );
            } else {
                inv.setItem(i,
                        IBuilder.buildItem(
                                warp.material,
                                "&f" + warp.getName(),
                                "",
                                "&e左键 &7- 传送到对应位置",
                                "&e右键 &7- &c取消收藏"
                        )
                );
            }
            ws++;
        }

        inv.setItem(34, back);

        p.openInventory(inv);
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player p && e.getInventory().getHolder() instanceof HYourLike) {
            e.setCancelled(true);
            int slot = e.getRawSlot();
            if (slot == 34) {
                AllWarps.open(p, 1);
                return;
            }
            if (wslot.contains(e.getRawSlot())) {
                var w = rawToID(e.getRawSlot(), 1);
                if (w == null) return;
                Warps warps = Like.getLiked(p).get(w);
                if (e.getCurrentItem().getType() == Material.BARRIER) {
                    if (Like.getLiked(p).size() <= w) return;
                    Like.delLiked(p, Integer.valueOf(warps.getId()));
                    open(p);
                } else {
                    if (Like.getLiked(p).size() <= w) return;
                    switch (e.getClick()) {
                        case LEFT -> {
                            p.teleport(warps.location);
                            HayerWarps.send(p, "&7已将您传送到 &f" + warps.name);
                        }
                        case RIGHT -> {
                            Like.delLiked(p, Integer.valueOf(warps.getId()));
                            HayerWarps.send(p, "&a您已取消喜欢 &f" + warps.name);
                            open(p);
                        }
                    }
                }
            }
        }
    }
}
