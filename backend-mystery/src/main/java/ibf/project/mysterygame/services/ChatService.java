package ibf.project.mysterygame.services;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import dev.ai4j.openai4j.OpenAiHttpException;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.service.AiServices;
import ibf.project.mysterygame.chatinterfaces.GameMaster;
import ibf.project.mysterygame.chatinterfaces.Judge;
import ibf.project.mysterygame.chatinterfaces.MysteryCreator;
import ibf.project.mysterygame.chatinterfaces.SceneNavigator;
import ibf.project.mysterygame.exceptions.AppException;
import ibf.project.mysterygame.models.game.MysteryArticle;
import ibf.project.mysterygame.models.game.MysteryCharacter;
import ibf.project.mysterygame.models.game.MysteryContext;
import ibf.project.mysterygame.models.game.MysteryInfo;
import ibf.project.mysterygame.models.game.MysteryWeapon;
import ibf.project.mysterygame.models.game.Outcome;
import ibf.project.mysterygame.repositories.GameRepository;
import ibf.project.mysterygame.utils.MysteryArticleWrapper;
import ibf.project.mysterygame.utils.MysteryCharacterWrapper;
import ibf.project.mysterygame.utils.MysteryWeaponWrapper;


@Service
public class ChatService {

    @Autowired
    private AiModelService modelService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private GameRepository gameRepo;

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);


    /* ------------- OpenAi Chat Creators----------- */
    public MysteryCreator createMysteryCreator(String apiKey) throws AppException {
        try {
            ChatLanguageModel model = modelService.buildOpenAIChatLanguageModelJson(apiKey);
            return AiServices.builder(MysteryCreator.class)
                .chatLanguageModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
        } catch (OpenAiHttpException e) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Invalid API key provided");
        }
    }

    public GameMaster createGameMaster(String apiKey) throws AppException {
        try {
            ChatLanguageModel model = modelService.buildOpenAIChatLanguageModel(apiKey);
            return AiServices.builder(GameMaster.class)
                .chatLanguageModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(30))
                .build();
        } catch (OpenAiHttpException e) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Invalid API key provided");
        }
    }

    public SceneNavigator createSceneNavigator(String apiKey) throws AppException {
        try {
            ChatLanguageModel model = modelService.buildOpenAIChatLanguageModel(apiKey);
            return AiServices.builder(SceneNavigator.class)
                .chatLanguageModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(30))
                .build();
        } catch (OpenAiHttpException e) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Invalid API key provided");
        }
    }

    public Judge createJudge(String apiKey) throws AppException {
        try {
            ChatLanguageModel model = modelService.buildOpenAIChatLanguageModelJson(apiKey);
            return AiServices.builder(Judge.class)
                .chatLanguageModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(5))
                .build();
        } catch (OpenAiHttpException e) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Invalid API key provided");
        }
    }

    public ImageModel createImageModel(String apiKey) throws AppException {
        try {
            return modelService.buildOpenAiImageModel(apiKey);
        } catch (OpenAiHttpException e) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Invalid API key provided");
        }
    }

    /* ----------- Parse chat to Object ------------- */
    public MysteryInfo generateMysteryInfo(MysteryCreator mysteryCreator, ImageModel model, String theme, String userId) {
        String mysteryId = generateRandomId();

		String context = mysteryCreator.generateContext(theme);
        String extractedContext = mysteryCreator.extractContext();
        String extractedWeapons = mysteryCreator.extractWeapons();
        String characters = mysteryCreator.generateCharacters();
        String extractedCharacters = mysteryCreator.extractCharacters();
        String articles = mysteryCreator.generateArticles();

        MysteryContext mysteryContext = new MysteryContext(mysteryId, String.format("%s %s", context, characters));
        MysteryInfo mysteryInfo = toMysteryInfo(mysteryId, extractedContext, extractedCharacters, articles, extractedWeapons, model);
        mysteryInfo.setUserId(userId);

        gameRepo.saveMysteryContext(mysteryContext);
        gameRepo.saveMystery(mysteryInfo);

        logger.info("Successfully generated mystery");

        return mysteryInfo;
    }

    public Outcome toOutcome(String response) {
        Gson gson = new Gson();
        return gson.fromJson(response, Outcome.class);
    }


    private MysteryInfo toMysteryInfo(String mysteryId, String context, String characters, String articles, String weapons, ImageModel model) {
        Gson gson = new Gson();
        MysteryInfo mysteryInfo = gson.fromJson(context, MysteryInfo.class);

        List<MysteryArticle> articleList = toArticles(articles);
        if (articleList.size() == 0) {
            logger.error("Error converting articles to Articles Object");
        }
        List<MysteryWeapon> weaponList = toWeapons(weapons);
        if (weaponList.size() == 0) {
            logger.error("Error converting weapons to Weapons Object");
        }

        String artStyle = mysteryInfo.getArtStyle();
        String coverPrompt = String.format("Generate an image in the art style of %s with the description of %s",  artStyle, mysteryInfo.getCoverImageDescription());
        String crimePrompt = String.format("Generate an image of a location with the art style: %s. Description of the location: %s. Subtly hide the following weapons in the location: %s.", artStyle, mysteryInfo.getCrimeLocationDescription(), weaponList.toString());
        String coverImageUrl = imageService.generateSingleImage(model, coverPrompt, mysteryInfo.getCoverImageDescription());
        String crimeImageUrl = imageService.generateSingleImage(model, crimePrompt, mysteryInfo.getCrimeLocationDescription());
        List<MysteryCharacter> characterList = toCharacters(characters, artStyle, model);
        if (characterList.size() < 4) {
            logger.error("Error converting characters to Characters Object");
        }

        mysteryInfo.setMysteryId(mysteryId);
        mysteryInfo.setCoverImageUrl(coverImageUrl);
        mysteryInfo.setCrimeSceneImageUrl(crimeImageUrl);
        mysteryInfo.setCharacters(characterList);
        mysteryInfo.setArticles(articleList);
        mysteryInfo.setWeapons(weaponList);

        return mysteryInfo;
    }


    private List<MysteryCharacter> toCharacters(String characters, String artStyle, ImageModel model) {
        Gson gson = new Gson();
        MysteryCharacterWrapper characterWrapper = gson.fromJson(characters, MysteryCharacterWrapper.class);
        List<MysteryCharacter> characterList = characterWrapper.getMysteryCharacters();
        // generate character image
        return imageService.generateCharacterImages(characterList, artStyle, model);  
    }


    private List<MysteryArticle> toArticles(String articles) {
        Gson gson = new Gson();
        MysteryArticleWrapper articleWrapper = gson.fromJson(articles, MysteryArticleWrapper.class);
        return articleWrapper.getMysteryArticles();
    }


    private List<MysteryWeapon> toWeapons(String weapons) {
        Gson gson = new Gson();
        MysteryWeaponWrapper weaponWrapper = gson.fromJson(weapons, MysteryWeaponWrapper.class);
        return weaponWrapper.getMysteryWeapons();
    }


    private String generateRandomId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }



    /* -------------- Test with Ollama ---------------*/
    // Test 
    public GameMaster createGameMasterOllama() {
        ChatLanguageModel model = modelService.buildOllamaChatLanguageModel();
        return AiServices.builder(GameMaster.class)
            .chatLanguageModel(model)
            .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
            .build();
    }

    // Test
    public SceneNavigator createSceneNavigatorOllama() throws AppException {
        ChatLanguageModel model = modelService.buildOllamaChatLanguageModel();
        return AiServices.builder(SceneNavigator.class)
            .chatLanguageModel(model)
            .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
            .build();
    }

    // Test
    public Judge createJudgeOllama() {
        ChatLanguageModel model = modelService.buildOllamaChatLanguageModel();
        return AiServices.builder(Judge.class)
            .chatLanguageModel(model)
            .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
            .build();
    }

}
