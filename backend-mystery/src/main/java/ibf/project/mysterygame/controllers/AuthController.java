package ibf.project.mysterygame.controllers;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ibf.project.mysterygame.configurations.authentication.UserAuthProvider;
import ibf.project.mysterygame.exceptions.AppException;
import ibf.project.mysterygame.models.Response;
import ibf.project.mysterygame.models.authentication.Credentials;
import ibf.project.mysterygame.models.authentication.ResetPassword;
import ibf.project.mysterygame.models.authentication.SignUp;
import ibf.project.mysterygame.models.authentication.User;
import ibf.project.mysterygame.models.authentication.UserEntity;
import ibf.project.mysterygame.services.UserService;

@RestController
@RequestMapping("/api")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthProvider userAuthProvider;

    @PostMapping("/login")
    public ResponseEntity<User> authenticateLogin(@RequestBody Credentials credentials) throws AppException {
        logger.info("Logging in... " + credentials,toString());

        User user = userService.login(credentials);
        user.setToken(userAuthProvider.createToken(user));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody SignUp signUp) throws AppException {
        logger.info("Registering... " + signUp,toString());

        User user = userService.register(signUp);
        user.setToken(userAuthProvider.createToken(user));
        return ResponseEntity.created(URI.create("/users/" + user.getId())).body(user);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Response> checkIfUserExists(@RequestBody String username) throws AppException {
        logger.info("Forgot password, checking if user exists... " + username.toString());

        UserEntity userEntity = userService.findUserByUsername(username);
        userService.sendPasswordResetEmail(userEntity);
        return ResponseEntity.ok(new Response("Password link sent to user's email"));
    }

    @GetMapping("/reset-password")
    public ResponseEntity<Response> showResetPasswordPage(@RequestParam("token") String token) {
        if (userService.isTokenValid(token)) {
            return ResponseEntity.ok(new Response("Password reset token is valid"));
        } else {
            return ResponseEntity.status(403).body(new Response("Invalid or expired token"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Response> resetPassword(@RequestBody ResetPassword userReset) throws AppException {
        if (userService.isTokenValid(userReset.token())) {
            logger.info("Resetting password... " + userReset.toString());
            userService.updatePassword(userReset);
            return ResponseEntity.ok(new Response("Password successfully updated"));
        } else {
            return ResponseEntity.status(403).body(new Response("Invalid or expired token"));
        }
    }

    
}
