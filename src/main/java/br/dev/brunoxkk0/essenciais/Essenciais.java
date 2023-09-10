package br.dev.brunoxkk0.essenciais;

import br.dev.brunoxkk0.essenciais.modules.EssenciaisModules;
import org.bukkit.plugin.java.JavaPlugin;

public class Essenciais extends JavaPlugin {

    private static Essenciais instance;

    public static Essenciais getInstance() {
        return instance;
    }

    public void onLoad(){
        instance = this;
        EssenciaisModules.onLoad(this);
    }

    public void onEnable(){
        EssenciaisModules.onEnable(this);
    }

    public void onDisable(){
        EssenciaisModules.onDisable(this);
    }

}
