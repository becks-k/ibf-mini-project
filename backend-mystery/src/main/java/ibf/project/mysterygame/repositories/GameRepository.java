package ibf.project.mysterygame.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import ibf.project.mysterygame.models.game.MysteryContext;
import ibf.project.mysterygame.models.game.MysteryInfo;

@Repository
public class GameRepository {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    public static final String MONGO_MYSTERY_COLLECTION = "mysteries";
    public static final String MONGO_GAME_MASTER_COLLECTION = "gamemaster"; 
    public static final String testUserId = "305e6b9d" ;    



    public MysteryInfo saveMystery(MysteryInfo mysteryInfo) {
        return mongoTemplate.insert(mysteryInfo, MONGO_MYSTERY_COLLECTION);
    }

    // returns empty list if none
    public List<MysteryInfo> getAllMysteries() {
        return mongoTemplate.findAll(MysteryInfo.class, MONGO_MYSTERY_COLLECTION);
    }

    public Optional<MysteryInfo> getMysteryById(String mysteryId) {
        return Optional.ofNullable(mongoTemplate.findById(mysteryId, MysteryInfo.class, MONGO_MYSTERY_COLLECTION));
    }

    // returns empty list if none
    // to fetch all pre-generated mysteries + user mysteries
    public List<MysteryInfo> getMysteryByUserId(String userId) {
        Query query = Query.query(new Criteria().orOperator(
            Criteria.where("userId").is(userId),
            Criteria.where("userId").is(testUserId))        
        );
        return mongoTemplate.find(query, MysteryInfo.class, MONGO_MYSTERY_COLLECTION);
    }

    public Long deleteMystery(String mysteryId) {
        Query query = Query.query(Criteria
        .where("_id")
        .is(mysteryId));
        return mongoTemplate.remove(query, MONGO_MYSTERY_COLLECTION).getDeletedCount();
        
    }



    public MysteryContext saveMysteryContext(MysteryContext mysteryContext) {
        return mongoTemplate.insert(mysteryContext, MONGO_GAME_MASTER_COLLECTION);
    }

    public Optional<MysteryContext> getMysteryContextById(String mysteryId) {
        return Optional.ofNullable(mongoTemplate.findById(mysteryId, MysteryContext.class, MONGO_GAME_MASTER_COLLECTION));
    }

    public Long deleteMysteryContext(String mysteryId) {
        Query query = Query.query(Criteria
        .where("_id")
        .is(mysteryId));
        return mongoTemplate.remove(query, MONGO_GAME_MASTER_COLLECTION).getDeletedCount();
    }


}
