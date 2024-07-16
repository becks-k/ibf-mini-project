package ibf.project.mysterygame.models.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Observation {
    private String sender;
    private String mysteryId;
    private String text;
}
