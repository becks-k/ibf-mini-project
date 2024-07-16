package ibf.project.mysterygame.models.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MysteryWeapon {
    private String id;
    private String weapon;
    private String description;
    private Boolean isUsedInCrime;
}
