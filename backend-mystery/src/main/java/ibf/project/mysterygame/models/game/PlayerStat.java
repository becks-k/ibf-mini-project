package ibf.project.mysterygame.models.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStat {
    private String id;
    private String username;
    private Integer totalScore;
    private Integer mysteriesPlayed;
    private Integer highestScore;
}
