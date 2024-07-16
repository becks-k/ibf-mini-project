package ibf.project.mysterygame.models.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiKeyRequest {
    private String token;
    private String apiKey;
}
