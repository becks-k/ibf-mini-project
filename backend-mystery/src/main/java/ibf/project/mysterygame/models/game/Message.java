package ibf.project.mysterygame.models.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String sender;
    private String suspectId;
    private String suspectName;
    private String text;
    private String mysteryId;
}
