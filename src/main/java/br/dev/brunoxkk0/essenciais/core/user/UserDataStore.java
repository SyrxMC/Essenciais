package br.dev.brunoxkk0.essenciais.core.user;

import br.dev.brunoxkk0.essenciais.core.data.DataStore;

import java.nio.file.Path;

public class UserDataStore extends DataStore<User> {

    public UserDataStore(Path folder) {
        super(User.class, folder);
    }

}
