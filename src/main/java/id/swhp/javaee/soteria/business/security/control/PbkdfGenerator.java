package id.swhp.javaee.soteria.business.security.control;

import id.swhp.javaee.soteria.business.security.boundary.HashGenerator;
import id.swhp.javaee.soteria.business.security.entity.HashServiceType;
import id.swhp.javaee.soteria.business.security.entity.HashType;
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
@HashServiceType(HashType.PBKDF)
public class PbkdfGenerator implements HashGenerator {

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

    @Override
    public String getHashedText(String text) {
        return this.pbkdfHash.generate(text.toCharArray());
    }

    @Override
    public boolean isHashedTextMatch(String text, String hashedText) {
        return this.pbkdfHash.verify(text.toCharArray(), hashedText);
    }
}
