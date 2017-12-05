package id.swhp.javaee.soteria.business.security.boundary;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.UUID.randomUUID;

import id.swhp.javaee.soteria.business.account.boundary.AccountStore;
import id.swhp.javaee.soteria.business.account.entity.Account;
import id.swhp.javaee.soteria.business.exception.boundary.InvalidUsernameException;
import id.swhp.javaee.soteria.business.security.entity.HashServiceType;
import id.swhp.javaee.soteria.business.security.entity.HashType;
import id.swhp.javaee.soteria.business.security.entity.SHAAlgorithm;
import id.swhp.javaee.soteria.business.security.entity.Sha;
import id.swhp.javaee.soteria.business.security.entity.Token;
import id.swhp.javaee.soteria.business.security.entity.TokenType;
import java.time.Instant;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Sukma Wardana
 * @since 1.0
 */
@Stateless
public class TokenStore {

    @PersistenceContext
    EntityManager em;

    @Inject
    @HashServiceType(HashType.SHA)
    @Sha(algorithm = SHAAlgorithm.SHA256)
    HashGenerator hash;

    @Inject
    AccountStore accountStore;

    public String generate(final String username, final String ipAddress, final String description,
            final TokenType tokenType) {

        String rawToken = randomUUID().toString();
        Instant expiration = now().plus(14, DAYS);

        save(rawToken, username, ipAddress, description, tokenType, expiration);

        return rawToken;
    }

    public void save(final String rawToken, final String username, final String ipAddress,
            final String description, final TokenType tokenType, final Instant expiration) {

        Account account = this.accountStore.getByUsername(username)
                .orElseThrow(InvalidUsernameException::new);

        Token token = new Token();

        token.setTokenHash(this.hash.getHashedText(rawToken));
        token.setExpiration(expiration);
        token.setDescription(description);
        token.setTokenType(tokenType);
        token.setIpAddress(ipAddress);

        account.addToken(token);

        this.em.merge(account);
    }

    public void remove(String token) {
        this.em.createNamedQuery(Token.REMOVE_TOKEN, Token.class)
                .setParameter("tokenHash", token).executeUpdate();
    }

    public void removeExpired() {
        this.em.createNamedQuery(Token.REMOVE_EXPIRED_TOKEN, Token.class).executeUpdate();
    }
}
