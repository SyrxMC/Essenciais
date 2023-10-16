package br.dev.brunoxkk0.essenciais.modules.user.config;

import br.dev.brunoxkk0.syrxmccore.core.config.Comment;
import br.dev.brunoxkk0.syrxmccore.core.config.ConfigFactory;
import br.dev.brunoxkk0.syrxmccore.libs.com.electronwill.nightconfig.core.conversion.Path;

public class UserConfig extends ConfigFactory.PathConfig {

    @Path("player.custom.displayNamePrefix")
    @Comment("Set the prefix for custom display names, can be empty, not null.")
    public String customDisplayNamePrefix = "¨";

}
