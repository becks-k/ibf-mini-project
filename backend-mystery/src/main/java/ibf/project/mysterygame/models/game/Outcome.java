package ibf.project.mysterygame.models.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Outcome {
    private String judgeResponse;
    private String caseOutcome;
    private String aftermath;
    private Integer score;
}
