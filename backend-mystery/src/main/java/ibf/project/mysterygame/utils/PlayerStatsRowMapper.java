package ibf.project.mysterygame.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ibf.project.mysterygame.models.game.PlayerStat;

public class PlayerStatsRowMapper implements RowMapper<PlayerStat> {

    @Override
    public PlayerStat mapRow(ResultSet rs, int rowNum) throws SQLException {
        PlayerStat ps = new PlayerStat();
        ps.setId(rs.getString("user_id"));
        ps.setUsername(rs.getString("username"));
        ps.setTotalScore(rs.getInt("total_score"));
        ps.setMysteriesPlayed(rs.getInt("total_mysteries"));
        ps.setHighestScore(rs.getInt("highest_score"));
        return ps;
    }
    
}
