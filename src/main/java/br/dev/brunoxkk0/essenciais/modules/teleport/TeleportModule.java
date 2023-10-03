package br.dev.brunoxkk0.essenciais.modules.teleport;

import br.dev.brunoxkk0.essenciais.Essenciais;
import br.dev.brunoxkk0.essenciais.core.config.ConfigFactory;
import br.dev.brunoxkk0.essenciais.core.module.IModule;
import br.dev.brunoxkk0.essenciais.modules.teleport.config.TeleportConfig;
import br.dev.brunoxkk0.essenciais.modules.user.config.UserConfig;
import br.dev.brunoxkk0.syrxmccore.core.commands.CommandManager;
import lombok.Getter;

import java.io.File;
import java.util.ResourceBundle;

public class TeleportModule implements IModule {

    public static ResourceBundle Lang = ResourceBundle.getBundle("br.dev.brunoxkk0.essenciais.lang.commands.Teleport.properties");

    @Getter
    private static TeleportConfig teleportConfig;


    @Override
    public String getName() {
        return "teleport";
    }

    @Override
    public void onLoad(Essenciais essenciais) {
    }

    @Override
    public void onEnable(Essenciais essenciais) {
        CommandManager.packagesRegister(TeleportModule.class.getPackage().getName() + ".commands", this.getClass().getClassLoader());
        teleportConfig = ConfigFactory.loadConfig(new File(essenciais.getDataFolder(), "config/commands/teleport.hocon").toPath(), TeleportConfig.class);

    }

    @Override
    public void onDisable(Essenciais essenciais) {

    }

    @Override
    public boolean isLoaded() {
        return true;
    }

}
