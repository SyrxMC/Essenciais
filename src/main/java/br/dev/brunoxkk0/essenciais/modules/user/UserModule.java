package br.dev.brunoxkk0.essenciais.modules.user;

import br.dev.brunoxkk0.essenciais.Essenciais;
import br.dev.brunoxkk0.essenciais.core.module.IModule;

import java.io.File;
import java.util.logging.Logger;

public class UserModule implements IModule {

    private static final Logger logger = Logger.getLogger("UserModule");

    private static UserModule instance;
    private static UserDataStore userDataStore;

    private boolean isLoaded = false;

    public static UserModule getInstance() {
        return instance;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static UserDataStore getUserDataStore() {
        return userDataStore;
    }

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

        userDataStore = new UserDataStore(new File(essenciais.getDataFolder(), "data/users.json").toPath());

        try{

            long start = System.currentTimeMillis();

            userDataStore.load();

            long duration = System.currentTimeMillis() - start;

            logger.info("Loaded [" + userDataStore.count() + "] users from disk. it's took " + duration + "ms.");

        }catch (Exception e){
            logger.warning("Error when loading user data: " + e.getMessage());
            return;
        }

        isLoaded = true;
    }

    @Override
    public void onEnable(Essenciais essenciais) {

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

}
