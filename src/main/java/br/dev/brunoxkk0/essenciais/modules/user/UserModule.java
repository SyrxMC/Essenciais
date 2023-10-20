package br.dev.brunoxkk0.essenciais.modules.user;

import br.dev.brunoxkk0.essenciais.Essenciais;
import br.dev.brunoxkk0.essenciais.core.module.IModule;
import br.dev.brunoxkk0.essenciais.modules.user.config.UserConfig;
import br.dev.brunoxkk0.essenciais.modules.user.data.UserDataStore;
import br.dev.brunoxkk0.essenciais.modules.user.event.PlayerConnectionHandler;
import br.dev.brunoxkk0.syrxmccore.core.config.ConfigFactory;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.logging.Logger;

public class UserModule implements IModule {

    @Getter
    private static final Logger logger = Logger.getLogger("UserModule");

    @Getter
    private static UserModule instance;
    @Getter
    private static UserDataStore userDataStore;
    @Getter
    private static UserConfig userConfig;

    private boolean isLoaded = false;

    public UserModule(){
        instance = this;
    }

    @Override
    public String getName() {
        return "User";
    }

    @Override
    public void onLoad(Essenciais essenciais) {

        logger.info("loading module...");

        userDataStore = new UserDataStore(new File(essenciais.getDataFolder(), "data/users/").toPath());

        try{

            long start = System.currentTimeMillis();

            userDataStore.load();

            long duration = System.currentTimeMillis() - start;

            logger.info("Loaded [" + userDataStore.count() + "] users from disk. it's took " + duration + "ms.");

        }catch (Exception e){
            logger.warning("Error when loading user data: " + e.getMessage());
            return;
        }

        userConfig = ConfigFactory.loadConfig(
                new File(essenciais.getDataFolder(), "modules/user/config.hocon").toPath(),
                UserConfig.class
        );

        isLoaded = true;
    }

    @Override
    public void onEnable(Essenciais essenciais) {
        Bukkit.getPluginManager().registerEvents(new PlayerConnectionHandler(), essenciais);
    }

    @Override
    public void onDisable(Essenciais essenciais) {
        try{
            long start = System.currentTimeMillis();

            userDataStore.save();

            long duration = System.currentTimeMillis() - start;

            logger.info("Saved [" + userDataStore.count() + "] users on disk. it's took " + duration + "ms.");

        }catch (Exception e){
            logger.warning("Error when saving user data: " + e.getMessage());
        }

    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    public void handleNewPlayer(Player player){

    }

}
