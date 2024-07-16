package ibf.project.mysterygame.chatinterfaces;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;


public interface MysteryCreator {

    @SystemMessage("""
        You are an imaginative writer who writes mystery stories for games. Set the context for a mystery game.

        Rules of the game:
            - 1 character is found to be a victim of the mystery.
            - There is 1 guilty character.
            - There are 2 innocent suspects.
            - There are 3 weapons, 1 of which is involved in the crime.
            - There is 1 crime scene location.
            - The player takes on the role of a detective.
            - The game allows players to search the location for evidence, interrogates suspects and reads articles to gather clues.
            - To win the game, the player must determine the guilty character, the weapon that caused the mystery, and the guilty character's motive.

        Do not give the detective a name.
            """)
    
    @UserMessage("""
        The mystery will have the following theme: {{it}}

        Using the game rules, generate factual information about the mystery This information will be used by a game facilitator to guide the interactions between the characters and the detective. The information will also be used to guide the facilitator in generating clues. Provide the following in JSON format:
            - Name of the mystery
            - Theme
            - Introduction to the mystery. This shall include the setting of the mystery, context of the mystery, victim of the mystery and what the detective needs to do.
            - Newspaper headline about the mystery
            - Brief image description, to be used to generate the cover of the mystery
            - Setting of mystery
            - Description crime location
            - Victim
            - Guilty character
            - Guilty character's motive
            - Innocent suspects not involved in mystery
            - List of weapons, with descriptions of each weapon. Indicate if weapon is involved in the crime.
            - 2 hints to help the detective solve the mystery
            - 1 red herring, to throw the detective off the case
            - Other information that is relevant to the mystery

            """)
    String generateContext(String theme);

    @UserMessage("""
        Using the factual information generated, extract information into the following JSON schema:
        {
            theme: string,
            artStyle: string,
            mysteryName: string,
            introduction: string,
            newsHeadline: string
            coverImageDescription: string,
            setting: string,
            crimeLocationDescription: string,
            victim: string,
            guilty: string,
            guiltyMotive: string,
            weaponUsedInCrime: string,
            hints: [string]
        }
            """)
    String extractContext();

    @UserMessage("""
        Using the factual information generated, extract information from the list of weapons in JSON with the following schema for each of the 3 weapons. Generate a unique 3 character id in lowercase for the field id.
        {
            id: string, 
            weapon: string,
            description: string,
            isUsedInCrime: boolean
        }
            
            """)
    String extractWeapons();

    @UserMessage("""
        Based on factual information about the mystery, generate details for a total of four characters, excluding the detective.
            - 1 victim
            - 1 guilty character
            - 2 innocent suspects        
        This information will be used by the game facilitator to guide the interactions between the characters and the detective. For each character, describe the following:
            - Id: Unique ID of 3 characters in lowercase
            - Name: Name of character
            - Character Description: Description used for generating character images
            - Personality: Key traits and behaviors of character.
            - Role or involvement in the mystery, in any 
            - Personal motive: State the character's motive. If the character is guilty, specify the motive for committing the crime. 
            - Relationship to other characters
            - Alibi: Where was the character at the time of the crime?
            - Activity: What was the character doing at the time of the crime?
            - Feelings about the crime: Describe how the character feels about the crime.
            - Knowledge of the crime: What does the character know about the crime?
            - Eager to talk: What are they eager to talk about? 
            - Lies: What information will the character lie about?
            - Other information that is relevant for the interrogation

            """)
    String generateCharacters();

    @UserMessage("""
        Using the generated character details, extract the information into the following JSON schema for each of the 4 characters:
        {
            id: string, 
            name: string,
            imageDescription: string,
            role: string,
            personality: string,
            victim: boolean,
            guilty: boolean,
            dead: boolean
        }

        """)
                
    String extractCharacters();

    @UserMessage("""
        Generate clues for the mystery in the form of light-hearted newspaper articles. Create 3 articles in total: 2 related to the mystery and 1 as a red herring. 
        
        Follow the JSON schema provided below for each article:
        {
            id: string,
            headline: string
            content: string
        } 
            """)
    String generateArticles();

}


