package br.dev.brunoxkk0.essenciais.modules.teleport.config;

import br.dev.brunoxkk0.syrxmccore.core.config.Comment;
import br.dev.brunoxkk0.syrxmccore.core.config.ConfigFactory;
import br.dev.brunoxkk0.syrxmccore.libs.com.electronwill.nightconfig.core.conversion.Path;

public class TeleportConfig extends ConfigFactory.PathConfig {

    @Path("teleport.invite.accept_time")
    @Comment("Time required to expire a teleport invite")
    public long acceptTime = 30000L;

    @Path("teleport.invite.warm_up_time")
    @Comment("Time required to not move to be teleported")
    public long warmUpTime = 5000L;

}
