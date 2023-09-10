package br.dev.brunoxkk0.essenciais.modules.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Position {
    private int x;
    private int y;
    private int z;
}
