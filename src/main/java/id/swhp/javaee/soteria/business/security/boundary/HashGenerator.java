package id.swhp.javaee.soteria.business.security.boundary;

/**
 *
 * @author Sukma Wardana
 * @since 1.0
 */
public interface HashGenerator {

    public String getHashedText(final String text);

    public boolean isHashedTextMatch(final String text, final String hashedText);
}
