package ibf.project.mysterygame.models.game;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerScore {
    @Id
    private Integer id;
    private String userId;
    private String mysteryId;
    private Integer score;
}
