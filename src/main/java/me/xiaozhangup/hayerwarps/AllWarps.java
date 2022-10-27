package me.xiaozhangup.hayerwarps;

import me.xiaozhangup.hayerwarps.holder.HAllWarps;
import me.xiaozhangup.hayerwarps.holder.HSignChoose;
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


    public static void open(Player p, Integer page) {
        curry.put(p, page);
        Inventory inv = Bukkit.createInventory(new HAllWarps(), 45, "全部岛屿传送点 (第 " + page + " 页)");

        for (int i = 0; i < 45; i++) {
            inv.setItem(i, bg);
        }

        int pa = (page - 1) * 15;
        for (Integer i : wslot) {
            Warps warps = Loader.warps.get(pa);
            inv.setItem(
                    i,
                    IBuilder.buildItem(
                            warps.material,
                            "&f" + warps.getName(),
                            "",
                            "&f创建者: &7" + warps.getOwner()
                    )
                    );
            pa++;
        }

        p.openInventory(inv);
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player p && e.getInventory().getHolder() instanceof HAllWarps) {
            e.setCancelled(true);
            //todo
        }
    }

}
