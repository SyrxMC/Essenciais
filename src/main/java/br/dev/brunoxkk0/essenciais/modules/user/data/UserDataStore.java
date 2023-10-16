package br.dev.brunoxkk0.essenciais.modules.user.data;

import br.dev.brunoxkk0.essenciais.core.data.DataStore;
import br.dev.brunoxkk0.essenciais.modules.user.data.model.User;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class UserDataStore extends DataStore<User> {

    private static final ArrayList<User> users = new ArrayList<>();

    @Getter
    private static UserDataStore instance;

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

    public void register(User user){
        users.add(user);
    }

    public void registerAndSave(User user) throws IOException {
        register(user);
        save(user);
    }

    @Override
    public Path calculateFilePath(User item) {
        return new File(getDataStorePath().toFile(), item.getUuid().toString() + ".json").toPath();
    }

}
