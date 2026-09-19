package devpilot.backend.service;

import devpilot.backend.exception.NotFoundException;
import devpilot.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import devpilot.backend.entity.User;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TextEncryptor textEncryptor;
    @Transactional(readOnly = true)
    public User requiredById(UUID id){
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }
    public String decryptAccessToken(User user){
        return textEncryptor.decrypt(user.getAccessToken());
    }
    private static Long toLong(Object value){
        if(value instanceof Number number){
            return number.longValue();
        }
        return Long.parseLong(String.valueOf(value));
    }
    @Transactional
    public User upsertFromGithub(Map<String, Object> attributes, String accessToken, String scopes) {
        Long githubId = toLong(attributes.get("id"));
        String login = String.valueOf(attributes.get("login"));
        String name = attributes.get("name") != null
                ? String.valueOf(attributes.get("name"))
                : login;
        String avatarUrl = attributes.get("avatar_url") != null
                ? String.valueOf(attributes.get("avatar_url"))
                : null;

        String encryptedToken = textEncryptor.encrypt(accessToken);

        User user = userRepository.findByGithubId(githubId).orElseGet(User::new);
        user.setGithubId(githubId);
        user.setGithubUsername(login);
        user.setDisplayName(name);
        user.setAvatarUrl(avatarUrl);
        user.setAccessToken(encryptedToken); // obscured by caption
        user.setTokenScope(scopes);         // obscured by caption
        return userRepository.save(user);
    }
}
