package id.swhp.javaee.soteria.business.account.boundary;

import id.swhp.javaee.soteria.business.account.entity.Account;
import id.swhp.javaee.soteria.business.exception.boundary.InvalidPasswordException;
import id.swhp.javaee.soteria.business.exception.boundary.InvalidUsernameException;
import id.swhp.javaee.soteria.business.security.boundary.HashGenerator;
import id.swhp.javaee.soteria.business.security.boundary.TokenStore;
import id.swhp.javaee.soteria.business.security.entity.TokenType;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import id.swhp.javaee.soteria.business.security.entity.HashServiceType;
import id.swhp.javaee.soteria.business.security.entity.HashType;
import id.swhp.javaee.soteria.business.security.entity.SHAAlgorithm;
import id.swhp.javaee.soteria.business.security.entity.Sha;

/**
 *
 * @author Sukma Wardana
 * @since 1.0
 */
@Stateless
public class AccountStore {

    @PersistenceContext
    EntityManager em;

    @Inject
    @HashServiceType(HashType.SHA)
    @Sha(algorithm = SHAAlgorithm.SHA256)
    HashGenerator tokenHash;

    @Inject
    @HashServiceType(HashType.PBKDF)
    HashGenerator passwordHash;

    @Inject
    TokenStore tokenStore;

    public void registerAccount(final String username, final String email, final String password) {
        String securedPassword = this.passwordHash.getHashedText(password);

        Account account = new Account(username, securedPassword, email);

        // Account should not activated by default.
        account.setActive(true);

        this.em.persist(account);
    }

    public Optional<Account> getByUsername(final String username) {
        try {
            return Optional.of(
                    this.em.createNamedQuery(Account.FIND_BY_USERNAME, Account.class)
                            .setParameter("username", username).getSingleResult()
            );
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public Optional<Account> getByEmail(final String email) {
        try {
            return Optional.of(
                    this.em.createNamedQuery(Account.FIND_BY_EMAIL, Account.class)
                            .setParameter("email", email).getSingleResult()
            );
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public Optional<Account> getByLoginToken(String loginToken, TokenType tokenType) {
        try {
            return Optional.of(
                    this.em.createNamedQuery(Account.FIND_BY_TOKEN, Account.class)
                            .setParameter("tokenHash", this.tokenHash.getHashedText(loginToken))
                            .setParameter("tokenType", tokenType)
                            .getSingleResult()
            );
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public Account getByUsernameAndPassword(String username, String password) {
        Account managedAccount = getByUsername(username).orElseThrow(InvalidUsernameException::new);

        if (!this.passwordHash.isHashedTextMatch(password, managedAccount.getPassword())) {
            throw new InvalidPasswordException();
        }

        return managedAccount;
    }
}
