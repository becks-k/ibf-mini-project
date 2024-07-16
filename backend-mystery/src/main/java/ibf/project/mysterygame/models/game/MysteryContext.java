package ibf.project.mysterygame.models.game;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MysteryContext {
    @Id
    private String id;
    private String context;
}
