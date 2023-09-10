package br.dev.brunoxkk0.essenciais.core.module;

import br.dev.brunoxkk0.essenciais.Essenciais;

public interface IModule {

    String getName();

    void onLoad(Essenciais essenciais);

    void onEnable(Essenciais essenciais);

    void onDisable(Essenciais essenciais);

    boolean isLoaded();

}
