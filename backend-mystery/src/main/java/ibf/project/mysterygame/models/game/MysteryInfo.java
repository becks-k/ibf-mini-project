package ibf.project.mysterygame.models.game;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MysteryInfo {
    @Id
    private String mysteryId; 

    // to set
    private String userId; 
    
    private String theme;
    private String artStyle;
    private String mysteryName;
    private String introduction;
    private String newsHeadline;
    private String victim;
    private String guilty;
    private String guiltyMotive;
    private String weaponUsedInCrime;
    private List<String> hints;
    
    private List<MysteryWeapon> weapons;
    private List<MysteryCharacter> characters;
    private List<MysteryArticle> articles;
    
    private String coverImageDescription;
    private String coverImageUrl; 
    private String crimeLocationDescription;
    private String crimeSceneImageUrl;
}
