package br.dev.brunoxkk0.essenciais.modules.user.data.model;

import br.dev.brunoxkk0.essenciais.core.data.DataStore;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends DataStore.Extensible {

    private UUID uuid;

    private String name;
    private String displayName;

    private long fistSeen;
    private long lastSeen;

    private String ip;

}
