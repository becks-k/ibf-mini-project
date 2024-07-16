package ibf.project.mysterygame.models.game;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MysteryCharacter {
    @Id
    private String id; 
    private String name;
    private String role;
    private String imageDescription;
    private String imageUrl;
    private String personality;
    private Boolean guilty;
    private Boolean victim;
    private Boolean dead;
}
