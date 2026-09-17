package devpilot.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

@Configuration
public class EncrytionConfig {

    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.text(
                "devpilot-secret",
                "1234567890abcdef"
        );
    }
}
