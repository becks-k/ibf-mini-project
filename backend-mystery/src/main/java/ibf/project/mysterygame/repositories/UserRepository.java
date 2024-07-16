package ibf.project.mysterygame.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ibf.project.mysterygame.models.authentication.UserEntity;
import ibf.project.mysterygame.utils.UserRowMapper;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate template;

    private static final String SQL_FIND_USERNAME = """
            select * from users where username = ?
            """;

    private static final String SQL_SAVE_USER = """
            insert into users (id, username, password, email) values (?, ?, ?, ?)
            """;
    
    private static final String SQL_UPDATE_PASSWORD = """
            update users set password = ? where username = ?
            """;

    
    public Optional<UserEntity> findUsername(String username) {
        List<UserEntity> users = template.query(SQL_FIND_USERNAME, new UserRowMapper(), username);
        if (users.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(users.get(0));
        }
    }

    public Integer saveUser(UserEntity user) {
        return template.update(SQL_SAVE_USER, user.getId(), user.getUsername(), user.getPassword(), user.getEmail());
    }

    public Integer updatePassword(UserEntity user) {
        return template.update(SQL_UPDATE_PASSWORD, user.getPassword(), user.getUsername());
    }

}
