package br.dev.brunoxkk0.essenciais.core.data;

@FunctionalInterface
public interface Expirable {

    void onExpire(String key, Object ... context);

}
