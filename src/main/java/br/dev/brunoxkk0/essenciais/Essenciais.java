package br.dev.brunoxkk0.essenciais;

import org.bukkit.plugin.java.JavaPlugin;

public class Essenciais extends JavaPlugin {

    private static Essenciais instance;

    public static Essenciais getInstance() {
        return instance;
    }

    public void onLoad(){
        instance = this;
    }

    public void onEnable(){}

    public void onDisable(){}

}
