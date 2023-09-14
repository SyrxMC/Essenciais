package br.dev.brunoxkk0.essenciais.modules.user.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
