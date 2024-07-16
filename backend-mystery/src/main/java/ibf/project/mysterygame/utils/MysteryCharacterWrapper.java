package ibf.project.mysterygame.utils;

import java.util.List;

import ibf.project.mysterygame.models.game.MysteryCharacter;

public class MysteryCharacterWrapper {
    private List<MysteryCharacter> characters;

    public List<MysteryCharacter> getMysteryCharacters() {
        return characters;
    }

    public void setMysteryCharacters(List<MysteryCharacter> characters) {
        this.characters = characters;
    }
}
