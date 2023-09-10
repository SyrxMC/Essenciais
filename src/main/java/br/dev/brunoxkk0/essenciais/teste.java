package br.dev.brunoxkk0.essenciais;

import br.dev.brunoxkk0.essenciais.core.user.Position;
import br.dev.brunoxkk0.essenciais.core.user.User;
import br.dev.brunoxkk0.essenciais.core.user.UserDataStore;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class teste {

    public static void main(String[] args) throws IOException {

        UserDataStore userDataStore = new UserDataStore(new File("user.json").toPath());

        userDataStore.load();

        System.out.println(userDataStore.getData());

        User user = User.builder().name("name1").lastName("lastName2").uuid(UUID.randomUUID()).build();

        Position position = user.getOrCreateExtension("lastPosition", Position.class);

        position.setX(5);
        position.setY(6);
        position.setZ(7);

        userDataStore.getData().add(user);
        userDataStore.save();

    }

}
