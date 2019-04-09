package edu.northeastern.ccs.im.user_group.converter;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.security.Key;

/**
 * CryptoConverter class encrypts/decrypts a attribute of an entity to be
 * stored in the database.
 * Source credit = https://thoughts-on-java.org/how-to-use-jpa-type-converter-to/
 */
@Converter
public class CryptoConverter implements AttributeConverter<String, String> {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final byte[] KEY = "CS5500ProjectSecurityKey".getBytes();

    @Override
    public String convertToDatabaseColumn(String password) {
        Key key = new SecretKeySpec(KEY, "AES");
        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            return Base64.encode(c.doFinal(password.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        Key key = new SecretKeySpec(KEY, "AES");
        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);
            return new String(c.doFinal(Base64.decode(dbData)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
