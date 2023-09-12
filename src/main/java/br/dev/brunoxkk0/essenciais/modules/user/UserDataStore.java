package br.dev.brunoxkk0.essenciais.modules.user;

import br.dev.brunoxkk0.essenciais.core.data.DataStore;
import br.dev.brunoxkk0.essenciais.modules.user.model.User;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class UserDataStore extends DataStore<User> {

    private static final ArrayList<User> users = new ArrayList<>();

    private static UserDataStore instance;

    public static UserDataStore getInstance() {
        return instance;
    }

    public UserDataStore(Path folder) {
        super(User.class, folder, users);
        instance = this;
    }

    public Optional<User> findByName(String name){
        return users.stream().filter(user -> user.getName().equalsIgnoreCase(name)).findFirst();
    }

    public Optional<User> findByUUID(UUID uuid){
        return users.stream().filter(user -> user.getUuid().equals(uuid)).findFirst();
    }

    public int count(){
        return users.size();
    }

    @Override
    public Path calculateFilePath(User item) {
        return new File(getDataStorePath().toFile(), item.getUuid().toString() + ".json").toPath();
    }

}
