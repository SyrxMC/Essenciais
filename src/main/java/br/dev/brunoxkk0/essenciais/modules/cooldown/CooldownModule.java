package br.dev.brunoxkk0.essenciais.modules.cooldown;

import br.dev.brunoxkk0.essenciais.Essenciais;
import br.dev.brunoxkk0.essenciais.core.data.CooldownManager;
import br.dev.brunoxkk0.essenciais.core.module.IModule;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class CooldownModule implements IModule {

    @Getter
    private static final Logger logger = Logger.getLogger("CooldownModule");

    @Getter
    private static CooldownManager cooldownManager;
    private boolean isLoaded = false;

    @Override
    public String getName() {
        return "cooldown";
    }

    @Override
    public void onLoad(Essenciais essenciais) {

        cooldownManager = new CooldownManager(
                new File(essenciais.getDataFolder(), "data/cooldown/").toPath()
        );

        try {
            cooldownManager.load();
        } catch (IOException e) {
            logger.warning("Error when loading cooldown data: " + e.getMessage());
        }

        isLoaded = true;

    }

    @Override
    public void onEnable(Essenciais essenciais) {
        cooldownManager.register("teste", System.currentTimeMillis(), 3000);
    }

    @Override
    public void onDisable(Essenciais essenciais) {
        try {
            cooldownManager.save();
        } catch (IOException e) {
            logger.warning("Error when saving cooldown data: " + e.getMessage());
        }
    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

}
