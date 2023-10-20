package br.dev.brunoxkk0.essenciais.modules;

import br.dev.brunoxkk0.essenciais.Essenciais;
import br.dev.brunoxkk0.essenciais.core.module.IModule;
import br.dev.brunoxkk0.essenciais.modules.cooldown.CooldownModule;
import br.dev.brunoxkk0.essenciais.modules.teleport.TeleportModule;
import br.dev.brunoxkk0.essenciais.modules.user.UserModule;

import java.util.HashSet;

public class EssenciaisModules {

    private static final HashSet<IModule> MODULES = new HashSet<>();
    private static boolean init = false;

    public static void initModules(){
        MODULES.add(new CooldownModule());
        MODULES.add(new UserModule());
        MODULES.add(new TeleportModule());
        init = true;
    }

    public static void onLoad(Essenciais essenciais){

        if(!init)
            initModules();

        MODULES.forEach(iModule -> iModule.onLoad(essenciais));
    }

    public static void onEnable(Essenciais essenciais){
        MODULES.forEach(iModule -> iModule.onEnable(essenciais));
    }

    public static void onDisable(Essenciais essenciais){
        MODULES.forEach(iModule -> iModule.onDisable(essenciais));
    }

}
