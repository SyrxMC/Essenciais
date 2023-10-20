package br.dev.brunoxkk0.essenciais.modules.teleport;

import br.dev.brunoxkk0.essenciais.Essenciais;
import br.dev.brunoxkk0.essenciais.core.module.IModule;
import br.dev.brunoxkk0.essenciais.modules.teleport.config.TeleportConfig;
import br.dev.brunoxkk0.essenciais.modules.teleport.config.TeleportLang;
import br.dev.brunoxkk0.syrxmccore.core.commands.CommandManager;
import br.dev.brunoxkk0.syrxmccore.core.config.ConfigFactory;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;

public class TeleportModule implements IModule {

    @Getter
    private static TeleportConfig teleportConfig;

    @Getter
    private static TeleportLang teleportLang;

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

        teleportConfig = ConfigFactory.loadConfig(
                new File(essenciais.getDataFolder(), "modules/teleport/config.hocon").toPath(),
                TeleportConfig.class
        );

        teleportLang = ConfigFactory.loadConfig(
                new File(essenciais.getDataFolder(), "modules/teleport/lang.hocon").toPath(),
                TeleportLang.class
        );

        CommandManager.getInstance().registerCompleteSupplier("tpa_requests", (sender, command, label, currentPos, token, args) ->
                TeleportManager.getList(((Player) sender).getUniqueId()).stream().map(uuid -> Bukkit.getOfflinePlayer(uuid).getName())
        );

    }

    @Override
    public void onDisable(Essenciais essenciais) {

    }

    @Override
    public boolean isLoaded() {
        return true;
    }

}
