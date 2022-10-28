package me.xiaozhangup.hayerwarps;

import me.xiaozhangup.hayerwarps.holder.HAllWarps;
import me.xiaozhangup.hayerwarps.holder.HSignChoose;
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

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AllWarps implements Listener {

    public static List<Integer> wslot = Arrays.asList(
            10,11,12,13,14,
            19,20,21,22,23,
            28,29,30,31,32
    );
    //一共有15个
    public static HashMap<Player, Integer> curry = new HashMap<>();
    public static final ItemStack bg = IBuilder.getBorder(Material.GRAY_STAINED_GLASS_PANE);
    public static final ItemStack pages = IBuilder.buildItem(Material.BOOK, "&f切换菜单页数", " ", "&e左键 &7- 上一页", "&e右键 &7- 下一页");
    public static final ItemStack your = IBuilder.buildItem(Material.TOTEM_OF_UNDYING, "&e你创建的传送点", " ", "&7查看你创建的传送点", " ", "&c这里将仅显示 7 个", "&c因为我们不建议您创建", "&c多于 7 个的传送点");
    public static final ItemStack like = IBuilder.buildItem(Material.GLOW_ITEM_FRAME, "&x&f&2&e&8&c&c你的喜欢", "", "&7查看你喜欢的传送点", " ", "&c这里将仅显示 15 个", "&c因为我们不允许您收藏", "&c多于 15 个的传送点");
    public static final ItemStack back = IBuilder.buildItem(Material.COMPASS, "&c返回空岛菜单");



    public static void open(Player p, Integer page) {
        curry.put(p, page);
        Inventory inv = Bukkit.createInventory(new HAllWarps(), 45, "全部岛屿传送点 (第 " + page + " 页)");

        for (int i = 0; i < 45; i++) {
            if (wslot.contains(i)) continue;
            inv.setItem(i, bg);
        }

        int pa = (page - 1) * 15;
        for (Integer i : wslot) {
            if (Loader.warps.size() <= pa) break;
            Warps warps = Loader.warps.get(pa);
            if (warps.material == Material.BARRIER) {
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
                                warps.material,
                                "&f" + warps.getName(),
                                "",
                                "&f创建者: &7" + warps.getOwner(),
                                "&e左键 &7- 传送到对应位置",
                                "&e右键 &7- 喜欢这个传送点"
                        )
                );
            }
            pa++;
        }

        inv.setItem(16, pages);
        inv.setItem(25, your);
        inv.setItem(34, like);
        inv.setItem(44, back);

        p.openInventory(inv);
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player p && e.getInventory().getHolder() instanceof HAllWarps) {
            e.setCancelled(true);
            if (wslot.contains(e.getRawSlot())) {
                switch (e.getClick()) {
                    case LEFT -> {
                        var w = rawToID(e.getRawSlot(), curry.get(p));
                        if (Loader.warps.size() <= w) return;
                        Warps warps = Loader.warps.get(w);
                        if (warps.material == Material.BARRIER) return;
                        p.teleport(warps.location);
                        HayerWarps.send(p, "&7已将您传送到 &f" + warps.name);
                        return;
                    }
                    case RIGHT -> {
                        var w = rawToID(e.getRawSlot(), curry.get(p));
                        if (Loader.warps.size() <= w) return;
                        Warps warps = Loader.warps.get(w);
                        if (warps.material == Material.BARRIER) return;
                        Like.addLiked(p, w);
                        p.closeInventory();
                        HayerWarps.send(p, "&a您已喜欢 &f" + warps.name);
                    }
                }

            }
            switch (e.getRawSlot()) {
                case 16 -> {
                    switch (e.getClick()) {
                        case LEFT -> {
                            if (curry.get(p) != 1) {
                                open(p, curry.get(p) - 1);
                            }
                        }
                        case RIGHT -> {
                            if (Loader.warps.size() > curry.get(p) * 15) {
                                open(p, curry.get(p) + 1);
                            }
                        }
                    }
                }
                case 25 -> {
                    YourWarps.open(p);
                }
                case 34 -> {
                    YourLike.open(p);
                }
                case 44 -> {
                    Bukkit.dispatchCommand(p, "is");
                }
            }
        }
    }

    public static @Nullable Integer rawToID(Integer raw, Integer page) {
        if (raw >= 10 && raw <=14) {
            return asID(raw - 10, page);
        }
        if (raw >= 19 && raw <= 23) {
            return asID(raw - 14, page);
        }
        if (raw >= 28 && raw <= 32) {
            return asID(raw - 18, page);
        }
        return null;
    }

    public static Integer asID(Integer s, Integer page) {
        return s + (15 * (page - 1));
    }

}
