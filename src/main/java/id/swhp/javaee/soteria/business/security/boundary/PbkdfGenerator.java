package id.swhp.javaee.soteria.business.security.boundary;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

/**
 *
 * @author Sukma Wardana
 * @version 1.0.0
 */
@Stateless
public class PbkdfGenerator {

    @Inject
    Pbkdf2PasswordHash pbkdfHash;

    @PostConstruct
    public void init() {
        Map<String, String> parameters = new HashMap<>();

        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");

        this.pbkdfHash.initialize(parameters);
    }

    public String getHashText(final String text) {
        char[] tempCharText = text.toCharArray();
        return this.pbkdfHash.generate(tempCharText);
    }

    public boolean isHashTextValid(final String text, final String hashedText) {
        char[] tempCharText = text.toCharArray();
        return this.pbkdfHash.verify(tempCharText, hashedText);
    }
}
