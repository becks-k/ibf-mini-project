package ibf.project.mysterygame.services;

import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import ibf.project.mysterygame.exceptions.AppException;
import ibf.project.mysterygame.models.game.ApiKeyRequest;
import jakarta.annotation.PostConstruct;

@Service
public class ApiKeyService {

    private Cache<String, String> cache;
    
    @PostConstruct
    public void init() {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .maximumSize(500)
                .build();
    }

    public void storeApiKey(ApiKeyRequest apiKeyRequest) {
        cache.put(apiKeyRequest.getToken(), apiKeyRequest.getApiKey());
    }

    public String getApiKey(String token) throws AppException {
        String apiKey = cache.getIfPresent(token);
        if (apiKey == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Error fetching API key");
        }
        return apiKey;
    }
    
    public void removeApiKey(String token) {
        cache.invalidate(token);
    }
}
