package com.amelinroman.webfluxsecurity.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * @author Amelin Roman
 * Компонент PBFDK2Encoder реализует интерфейс PasswordEncoder для кодирования и сравнения
 * паролей с использованием PBKDF2 и HmacSHA512.
 */

@Component
public class PBFDK2Encoder implements PasswordEncoder {

    @Value("${jwt.password.encoder.secret}")
    private String secret;
    @Value("${jwt.password.encoder.iterator}")
    private Integer iterator;
    @Value("${jwt.password.encoder.keylength}")
    private Integer keyLength;

    private static final String SECRET_KEY_INSTANCE = "PBKDF2WithHmacSHA512";

    /**
     * Кодирует пароль с использованием алгоритма PBKDF2 и HmacSHA512.
     *
     * @param rawPassword некодированный пароль, который требуется закодировать.
     * @return закодированная строка пароля.
     * @throws RuntimeException если алгоритм шифрования недоступен или ключ некорректен.
     */
    @Override
    public String encode(CharSequence rawPassword) {
        try {
            byte[] result = SecretKeyFactory.getInstance(SECRET_KEY_INSTANCE)
                    .generateSecret(new PBEKeySpec(rawPassword.toString().toCharArray(),
                            secret.getBytes(), iterator, keyLength)).getEncoded();
            return Base64.getEncoder().encodeToString(result);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Сравнивает некодированный пароль с закодированным паролем.
     *
     * @param rawPassword     некодированный пароль для проверки.
     * @param encodedPassword закодированный пароль для сравнения.
     * @return true, если пароли совпадают, иначе false.
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
