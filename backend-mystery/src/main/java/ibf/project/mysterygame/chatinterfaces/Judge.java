package ibf.project.mysterygame.chatinterfaces;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;


public interface Judge {
    @UserMessage("""
        You are a game facilitator for a mystery game. Below is information on the crime:

        {{it}}

        From the crime information, extract the true accusations in JSON format:
            - Who is the guilty character?
            - What was the weapon used in the crime?
            - What is the guilty character's motive?
        """)
    String extractAccusation(String mysteryContext);        
            
    @UserMessage("""
        This is the mystery's true guilty character, weapon used, and motive: {{accuseFacts}}

        Evaluate the detective's accusation below. Construct a response based on the following description:
            - Judge's response: Describe your evaluation of the case. Indicate which of the three - guilty character, weapon and motive - the detective got right or wrong. Do not reveal the answers for incorrect accusations. 
            Example:
            DO NOT state "However, the weapon used in the crime was not The Whispering Palette Knife but The Phantom Brush."
            - Case outcome: If the detective got all 3 accusations correct, respond with "Accusation accepted". If the detective got at least 1 accusation wrong, respond with "Accusation rejected".
            - Aftermath of characters: Write a dramatic epilogue describing what happens to the detective in future tense. If the wrong character is accused, indicate that the true perpetrator is still at large. Do not reveal the identity so the game can be played again.
            - Score: On a scale of 0 to 100, score the detective based on how accurate the detective's accusations are. (0 for all wrong, 100 for all correct, and proportional scores for partial correctness).
        
        This is the detective's accusations: {{accusation}}

        Return the output in JSON with the schema:
        {
        judgeResponse: string  
        caseOutcome: string
        aftermath: string
        score: integer      
        }

            """)
    String accuse(@V("accuseFacts") String accuseFacts, @V("accusation") String accusation);

    
}
