package ibf.project.mysterygame.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ibf.project.mysterygame.exceptions.AppException;
import ibf.project.mysterygame.models.game.MysteryContext;
import ibf.project.mysterygame.models.game.MysteryInfo;
import ibf.project.mysterygame.models.game.PlayerScore;
import ibf.project.mysterygame.models.game.PlayerStat;
import ibf.project.mysterygame.repositories.GameHistoryRepository;
import ibf.project.mysterygame.repositories.GameRepository;

@Service
public class GameService {
    
    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private GameHistoryRepository gameHistoryRepo;

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);
    
    public MysteryInfo getMysteryById(String mysteryId) throws AppException {
        Optional<MysteryInfo> optional = gameRepo.getMysteryById(mysteryId);
        if (optional.isEmpty()) {
            logger.error(String.format("No mystery of id found: %s"), mysteryId);
        }
        return optional.get();
    }
    
    public List<MysteryInfo> getMysteriesByUserId(String userId) {
        return gameRepo.getMysteryByUserId(userId);
    }

    public void deleteMystery(String mysteryId) throws AppException {
        Long deletedMystery = gameRepo.deleteMystery(mysteryId);
        Long deletedContext = gameRepo.deleteMysteryContext(mysteryId);
        logger.info("Deleted mystery info and context..." + deletedMystery + " " + deletedContext);
        if (deletedMystery != 1 && deletedContext != 1) {
            throw new AppException(HttpStatus.NOT_FOUND, "Error deleting mystery. Please try again.");
        }
    }

    public MysteryContext getMysteryContext(String mysteryId) {
        Optional<MysteryContext> optional = gameRepo.getMysteryContextById(mysteryId);
        if (optional.isEmpty()) {
            logger.error(String.format("No mystery of id found: %s"), mysteryId);
        }
        return optional.get();
    }

    public List<PlayerStat> getLeaderboard() {
        return gameHistoryRepo.getPlayerStats();
    }

    public Integer savePlayerScore(String userId, String mysteryId, Integer score) {
        PlayerScore ps = new PlayerScore();
        ps.setUserId(userId);
        ps.setMysteryId(mysteryId);
        ps.setScore(score);
        return gameHistoryRepo.saveGameScore(ps);
    }


}
