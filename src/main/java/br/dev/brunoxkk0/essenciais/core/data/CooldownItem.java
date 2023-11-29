package br.dev.brunoxkk0.essenciais.core.data;

import lombok.Data;

@Data
public class CooldownItem {
    private final String key;
    private final long time;
    private final long duration;
}
