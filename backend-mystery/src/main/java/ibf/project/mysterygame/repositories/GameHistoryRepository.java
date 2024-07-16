package ibf.project.mysterygame.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ibf.project.mysterygame.models.game.PlayerScore;
import ibf.project.mysterygame.models.game.PlayerStat;
import ibf.project.mysterygame.utils.PlayerStatsRowMapper;

@Repository
public class GameHistoryRepository {

    @Autowired
    private JdbcTemplate template;

    private static final String SQL_SAVE_GAME_SCORE= """
            insert into game_history (user_id, mystery_id, score) values (?, ?, ?)
            """;
    
    private static final String SQL_GET_PLAYERSTATS = """
            select g.user_id, u.username, SUM(g.score) as total_score, count(g.mystery_id) as total_mysteries, MAX(g.score) as highest_score
            from game_history g
            inner join users u
            on g.user_id = u.id
            group by g.user_id
            having count(g.mystery_id)
            order by total_score desc
            """;

    public Integer saveGameScore(PlayerScore playerScore) {
        return template.update(SQL_SAVE_GAME_SCORE, playerScore.getUserId(), playerScore.getMysteryId(), playerScore.getScore());
    } 


    public List<PlayerStat> getPlayerStats() {
        return template.query(SQL_GET_PLAYERSTATS, new PlayerStatsRowMapper());
    }
}


