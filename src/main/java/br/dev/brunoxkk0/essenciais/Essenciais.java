package br.dev.brunoxkk0.essenciais;

import br.dev.brunoxkk0.essenciais.modules.EssenciaisModules;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class Essenciais extends JavaPlugin {

    @Getter
    private static Essenciais instance;

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
