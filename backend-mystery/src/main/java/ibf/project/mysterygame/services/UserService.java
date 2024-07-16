package ibf.project.mysterygame.services;

import java.nio.CharBuffer;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ibf.project.mysterygame.configurations.authentication.UserAuthProvider;
import ibf.project.mysterygame.exceptions.AppException;
import ibf.project.mysterygame.models.authentication.Credentials;
import ibf.project.mysterygame.models.authentication.ResetPassword;
import ibf.project.mysterygame.models.authentication.SignUp;
import ibf.project.mysterygame.models.authentication.User;
import ibf.project.mysterygame.models.authentication.UserEntity;
import ibf.project.mysterygame.repositories.UserRepository;
import ibf.project.mysterygame.utils.UserMapper;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserAuthProvider userAuthProvider;

    @Autowired
    private EmailService emailService;

    @Value("${reset.password.url}")
    private String resetPasswordUrl;



    public User login(Credentials credentials) throws AppException {
        // check if user exists in database
        Optional<UserEntity> userOpt = userRepo.findUsername(credentials.username());
        if (userOpt.isEmpty()) {
            throw new AppException(HttpStatus.NOT_FOUND, "User does not exist");
        }

        UserEntity user =  userOpt.get();
        // if password matches that in database, map user to UserDto
        if (passwordEncoder.matches(CharBuffer.wrap(credentials.password()), user.getPassword())) {
            return userMapper.toUser(user);
        }
        
        throw new AppException(HttpStatus.BAD_REQUEST,"Invalid password");
    }

    public User register(SignUp signUp) throws AppException {
        Optional<UserEntity> userOpt = userRepo.findUsername(signUp.username());
        if (userOpt.isPresent()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        UserEntity user = userMapper.signUpToUserEntity(signUp);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUp.password())));
        user.setId(generateRandomId());
        
        Integer success = userRepo.saveUser(user);
        if (success == 0) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Registration failed");
        }
        return userMapper.toUser(user);
    }

    public UserEntity findUserByUsername(String username) throws AppException {
        Optional<UserEntity> userOpt = userRepo.findUsername(username);
        if (userOpt.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "User does not exist");
        }
        return userOpt.get();
    }

    public void sendPasswordResetEmail(UserEntity userEntity) {
        User user = userMapper.toUser(userEntity);
        String token = userAuthProvider.createToken(user);
        String resetUrl = String.format("%s?token=%s", resetPasswordUrl, token);
        emailService.sendEmailMessage(userEntity.getEmail(), 
            "Password Reset Request", 
            "You have requested to reset your password for MysteryGame.\nClick on the link to reset your password:\n" + resetUrl);
    }

    public void updatePassword(ResetPassword userReset) throws AppException {
        String username = getUserFromToken(userReset.token());
        Optional<UserEntity> userOpt = userRepo.findUsername(username);
        if (userOpt.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "User does not exist");
        }

        UserEntity user = userOpt.get();
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userReset.password())));
        Integer success = userRepo.updatePassword(user);
        if (success == 0) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Password update failed");
        }
    }

    public String getUserFromToken(String token) {
        return userAuthProvider.extractUser(token);
    }

    public Boolean isTokenValid(String token) {
        return !userAuthProvider.isTokenExpired(token);
    }

    private String generateRandomId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
