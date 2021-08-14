package rafnews.backend.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import rafnews.backend.model.User;
import rafnews.backend.repositories.user.IUserRepository;

import org.apache.commons.codec.digest.DigestUtils;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class UserService {

    @Inject
    IUserRepository userRepository;

    public String login(String email, String password)
    {
//      Hash pass on front and send

        User user = this.userRepository.findUser(email);
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }

        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + 24*60*60*1000); // One day

        Algorithm algorithm = Algorithm.HMAC256("secret");

        // JWT-om mozete bezbedono poslati informacije na FE
        // Tako sto sve sto zelite da posaljete zapakujete u claims mapu
        return JWT.create()
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withSubject(email)
       //         .withClaim("type", user.getType())
                .sign(algorithm);
    }

    public boolean isAuthorized(String token){
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        DecodedJWT jwt = verifier.verify(token);

        String email = jwt.getSubject();
//       jwt.getClaim("role").asString();

        User user = this.userRepository.findUser(email);

        if (user == null){
            return false;
        }

        return true;
    }



    public User addUser(User user) {
        return this.userRepository.addUser(user);
    }

    public List<User> allUser(int page, int perPage) {
        return this.userRepository.allUser( page,  perPage);
    }

    public User findUser(String name) {
        return this.userRepository.findUser(name);
    }

    public Integer userActivity(String email) {

        return this.userRepository.userActivity(email);
    }

    public User updateUser(User user, String email) {
        return this.userRepository.updateUser(user, email);
    }


}