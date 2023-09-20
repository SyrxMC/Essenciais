package br.dev.brunoxkk0.essenciais.modules.user.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Location;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Position {

    private String world;

    private double x;
    private double y;
    private double z;

    private double yaw;
    private double pitch;

    public void copyLocation(Location location){
        this.setX(location.getX());
        this.setY(location.getY());
        this.setZ(location.getZ());

        this.setYaw(location.getYaw());
        this.setPitch(location.getPitch());

        this.setWorld(location.getWorld().getUID().toString());
    }

}
