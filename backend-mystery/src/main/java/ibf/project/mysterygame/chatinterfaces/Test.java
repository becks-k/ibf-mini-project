package ibf.project.mysterygame.chatinterfaces;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface Test {
    
    @SystemMessage("""
        You are a game creator for a mystery game. 
            """)

    @UserMessage("""
        This is the game information: {{it}}

        Generate clues for the mystery in the form of light-hearted and humorous newspaper articles. Create 3 articles in total: 2 related to the mystery and 1 as a red herring. 
        
        Follow the JSON schema provided below for each article:
        {
            id: string,
            headline: string
            content: string
        } 
            """)
    String chat(String gameInfo);
}
