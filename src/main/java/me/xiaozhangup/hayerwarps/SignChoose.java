package me.xiaozhangup.hayerwarps;

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

import java.util.HashMap;

public class SignChoose implements Listener {

    public static HashMap<Player, String> warpc = new HashMap<>();
    public static final ItemStack bg = IBuilder.getBorder(Material.GRAY_STAINED_GLASS_PANE);

    public static void open(Player p, String name) {
        warpc.put(p, name);
        Inventory inv = Bukkit.createInventory(new HSignChoose(), 27, "为你的传送点选择一个图标");

        for (int i = 0; i < 27; i++) {
            inv.setItem(i, bg);
        }

        inv.setItem(10, new ItemStack(Material.OAK_SIGN));
        inv.setItem(11, new ItemStack(Material.SPRUCE_SIGN));
        inv.setItem(12, new ItemStack(Material.ACACIA_SIGN));
        inv.setItem(13, new ItemStack(Material.BIRCH_SIGN));
        inv.setItem(14, new ItemStack(Material.DARK_OAK_SIGN));
        inv.setItem(15, new ItemStack(Material.MANGROVE_SIGN));
        inv.setItem(16, new ItemStack(Material.ACACIA_SIGN));

        p.openInventory(inv);
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player p && e.getInventory().getHolder() instanceof HSignChoose) {
            e.setCancelled(true);
            if (e.getRawSlot() <= 16 && e.getRawSlot() >= 10) {
                if (e.getCurrentItem() == null) return;
                p.closeInventory();
                HayerWarps.send(p, "&f成功设置了一个传送点!");
                Warps warps = new Warps(p.getName() ,p.getLocation(), e.getCurrentItem().getType(), warpc.get(p), String.valueOf(Loader.warps.size()));
                Loader.addWarp(warps);
            }
        }
    }

}
