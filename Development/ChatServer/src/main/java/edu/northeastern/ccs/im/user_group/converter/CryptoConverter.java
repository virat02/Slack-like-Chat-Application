package edu.northeastern.ccs.im.user_group.converter;
import java.util.Base64;
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
    // Algorithm used by the Cipher
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    //Unique Key that is used in Converting the given attribute
    private static final byte[] KEY = "CS5500ProjectSecurityKey".getBytes();

    /**
     *  convertToDatabaseColumn encrypts the given attribute and
     *  returns this encrypted string which will be stored in teh database
     * @param password
     * @return an encrypted string of the password using the Cipher
     */
    @Override
    public String convertToDatabaseColumn(String password) {
        Key key = new SecretKeySpec(KEY, "AES");
        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(c.doFinal(password.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  convertToEntityAtrribute fetched the encrypted data from the database and
     *  decrypts it to the original String to be stored in the Attribute of an Entity.
     * @param dbData data fetched from database
     * @return decrypted String of the fetched database column value
     */
    @Override
    public String convertToEntityAttribute(String dbData) {
        Key key = new SecretKeySpec(KEY, "AES");
        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);
            return new String(c.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
