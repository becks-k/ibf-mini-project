package ibf.project.mysterygame.models.game;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MysteryArticle {
    @Id
    private String id;
    private String headline;
    private String content;
}
