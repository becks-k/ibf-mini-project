package ibf.project.mysterygame.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibf.project.mysterygame.exceptions.AppException;
import ibf.project.mysterygame.models.Response;
import ibf.project.mysterygame.models.game.ApiKeyRequest;
import ibf.project.mysterygame.services.ApiKeyService;


@RestController
@RequestMapping("/api")
public class ApiKeyController {

    @Autowired
    private ApiKeyService apiKeyService;

    private static final Logger logger = LoggerFactory.getLogger(ApiKeyController.class);
    
    @PostMapping("/cache-api-key")
    public ResponseEntity<Response> cacheApiKey(@RequestHeader("Authorization") String token, @RequestBody String apiKey) {
        ApiKeyRequest apiKeyRequest = new ApiKeyRequest(token, apiKey);
        logger.info("Caching key..." + apiKeyRequest.toString());
        apiKeyService.storeApiKey(apiKeyRequest);
        return ResponseEntity.ok(new Response("Successfully set API key"));
    }

    @DeleteMapping("/remove-api-key")
    public ResponseEntity<Response> logout(@RequestHeader("Authorization") String token) {
        apiKeyService.removeApiKey(token);
        logger.info("Successfully removed api key from cache");
        return ResponseEntity.ok(new Response("Successfully removed apikey"));
    }

    @GetMapping("/retrieve-api-key")
    public ResponseEntity<Response> getApiKey(@RequestHeader("Authorization") String token) throws AppException {
        String apikey = apiKeyService.getApiKey(token);
        return ResponseEntity.ok(new Response("API key is currently in cache: " + apikey));
    }
}
