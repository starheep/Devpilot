package devpilot.backend.service;

import devpilot.backend.exception.NotFoundException;
import devpilot.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

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
}
