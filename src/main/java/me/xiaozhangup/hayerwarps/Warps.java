package me.xiaozhangup.hayerwarps;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Warps {

    String name;
    String owner;
    Location location;
    Material material;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public Warps(String p, Location pos, Material icon, String displayname, String i) {
        owner = p;
        location = pos;
        material = icon;
        name = displayname;
        id = i;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
