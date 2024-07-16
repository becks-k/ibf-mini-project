package ibf.project.mysterygame.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import dev.langchain4j.model.image.ImageModel;
import ibf.project.mysterygame.chatinterfaces.GameMaster;
import ibf.project.mysterygame.chatinterfaces.Judge;
import ibf.project.mysterygame.chatinterfaces.MysteryCreator;
import ibf.project.mysterygame.chatinterfaces.SceneNavigator;
import ibf.project.mysterygame.exceptions.AppException;
import ibf.project.mysterygame.models.Response;
import ibf.project.mysterygame.models.game.Accuse;
import ibf.project.mysterygame.models.game.Message;
import ibf.project.mysterygame.models.game.MysteryContext;
import ibf.project.mysterygame.models.game.MysteryInfo;
import ibf.project.mysterygame.models.game.Observation;
import ibf.project.mysterygame.models.game.Outcome;
import ibf.project.mysterygame.models.game.PlayerStat;
import ibf.project.mysterygame.services.ApiKeyService;
import ibf.project.mysterygame.services.ChatService;
import ibf.project.mysterygame.services.GameService;


@RestController
@RequestMapping("/api")
public class GameController {
    
    @Autowired
    private ChatService chatService;

    @Autowired
    private GameService gameService;

    @Autowired
    private ApiKeyService apiKeyService;

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);


    @GetMapping("/generate/{userId}")
    public ResponseEntity<MysteryInfo> generateMystery(
        @RequestHeader("Authorization") String token, 
        @PathVariable("userId") String userId, 
        @RequestParam String theme) throws AppException {
        
        logger.info("Fetching API key...");
        String apiKey = apiKeyService.getApiKey(token);

        logger.info("Generating mystery...");
        MysteryCreator mysteryCreator = chatService.createMysteryCreator(apiKey);
        ImageModel imageModel = chatService.createImageModel(apiKey);
        MysteryInfo mystery = chatService.generateMysteryInfo(mysteryCreator, imageModel, theme, userId);

        return ResponseEntity.ok(mystery);
    }


    @GetMapping("/mysteries/{userId}")
    public ResponseEntity<List<MysteryInfo>> getMysteries(@PathVariable("userId") String userId) {
        List<MysteryInfo> mysteries = gameService.getMysteriesByUserId(userId);
        return ResponseEntity.ok(mysteries);
    }

    @PostMapping("/chat")
    public ResponseEntity<Message> chat(
        @RequestHeader("Authorization") String token, 
        @RequestBody Message message) throws AppException {
        
        logger.info("Fetching API key...");
        String apiKey = apiKeyService.getApiKey(token);

        logger.info("Chatting with game master... " + message.toString());
        MysteryContext mysteryContext = gameService.getMysteryContext(message.getMysteryId());      
        GameMaster gameMaster = chatService.createGameMaster(apiKey);
        String response = gameMaster.chat(mysteryContext.getContext(), message.getSuspectName(), message.getText());
        message.setText(response);
        message.setSender("AI");
        logger.info("Sending response... " + message.toString());

        return ResponseEntity.ok(message);
    }


    @PostMapping("/search")
    public ResponseEntity<Observation> searchScene(
        @RequestHeader("Authorization") String token, 
        @RequestBody Observation observation) throws AppException {
        
        logger.info("Fetching API key...");
        String apiKey = apiKeyService.getApiKey(token);

        logger.info("Chatting with scene navigator... " + observation.toString());
        MysteryContext mysteryContext = gameService.getMysteryContext(observation.getMysteryId());
        SceneNavigator navigator = chatService.createSceneNavigator(apiKey);
        String response = navigator.search(mysteryContext.getContext(), observation.getText());
        observation.setSender("AI");
        observation.setText(response);
        logger.info("Sending observation... " + observation.toString());

        return ResponseEntity.ok(observation);
    }


    @PostMapping("/accuse")
    public ResponseEntity<Outcome> accuse(
        @RequestHeader("Authorization") String token, 
        @RequestBody Accuse accuse) throws AppException {
        
        logger.info("Fetching API key...");
        String apiKey = apiKeyService.getApiKey(token);
        
        logger.info("Judging... " + accuse.toString());
        MysteryContext mysteryContext = gameService.getMysteryContext(accuse.mysteryId());
        Judge judge = chatService.createJudge(apiKey);
        String accuseFacts = judge.extractAccusation(mysteryContext.toString());  
        String response = judge.accuse(accuseFacts, accuse.toString());
        logger.info("Accusation facts: " + accuseFacts);
        logger.info("Judge's response: " + response);
        
        Outcome outcome = chatService.toOutcome(response);
        gameService.savePlayerScore(accuse.userId(), accuse.mysteryId(), outcome.getScore());
        return ResponseEntity.ok(outcome);

    }

    
    @DeleteMapping("/delete/{mysteryId}")
    public ResponseEntity<Response> deleteMystery(@PathVariable("mysteryId") String mysteryId) throws AppException {
        
        logger.info("Deleting mystery... " + mysteryId);
        gameService.deleteMystery(mysteryId);
        
        return ResponseEntity.ok(new Response("Delete successful"));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<PlayerStat>> getLeaderboard() {
        
        logger.info("Retrieving player stats... " );
        List<PlayerStat> leaderboard = gameService.getLeaderboard();
        return ResponseEntity.ok(leaderboard);
    }

    /* ---------- Test functions --------------- */

    // @PostMapping("/chat")
    // public ResponseEntity<Message> chat(
    //     @RequestHeader("Authorization") String token, 
    //     @RequestBody Message message) throws AppException {
        
    //     System.out.println("GameController: Chatting... message: " + message.toString());
    //     MysteryContext mysteryContext = gameService.getMysteryContext(message.getMysteryId());      
    //     GameMaster gameMaster = chatService.createGameMasterOllama();
    //     String response = gameMaster.chat(mysteryContext.getContext(), message.getSuspectName(), message.getText());
    //     message.setText(response);
    //     message.setSender("AI");

    //     return ResponseEntity.ok(message);
    // }


    // @PostMapping("/search")
    // public ResponseEntity<Observation> searchScene(
    //     @RequestHeader("Authorization") String token, 
    //     @RequestBody Observation observation) throws AppException {
        
    //     System.out.println("GameController: Searching... message: " + observation.toString());
    //     MysteryContext mysteryContext = gameService.getMysteryContext(observation.getMysteryId());
    //     SceneNavigator navigator = chatService.createSceneNavigatorOllama();
    //     String response = navigator.search(mysteryContext.getContext(), observation.getText());
    //     observation.setSender("AI");
    //     observation.setText(response);

    //     return ResponseEntity.ok(observation);
    // }


}
