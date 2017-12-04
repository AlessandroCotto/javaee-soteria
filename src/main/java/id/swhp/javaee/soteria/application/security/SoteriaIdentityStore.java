package id.swhp.javaee.soteria.application.security;

import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import static javax.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;

import id.swhp.javaee.soteria.business.account.boundary.AccountStore;
import id.swhp.javaee.soteria.business.account.entity.Account;
import id.swhp.javaee.soteria.business.exception.boundary.AccountNotVerifiedException;
import id.swhp.javaee.soteria.business.exception.boundary.InvalidCredentialException;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.security.enterprise.credential.CallerOnlyCredential;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

/**
 *
 * @author Sukma Wardana
 * @since 1.0
 */
@ApplicationScoped
@Default
public class SoteriaIdentityStore implements IdentityStore {

    // call our EJB service to validate the account
    @Inject
    AccountStore accountStore;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        try {

            // check if the credential was UsernamePasswordCredential
            if (credential instanceof UsernamePasswordCredential) {
                String username = ((UsernamePasswordCredential) credential).getCaller();
                String password = ((UsernamePasswordCredential) credential).getPasswordAsString();

                return validate(this.accountStore.getByUsernameAndPassword(username, password));
            }

            // check if the credential was UsernamePasswordCredential
            if (credential instanceof CallerOnlyCredential) {
                String username = ((CallerOnlyCredential) credential).getCaller();

                return validate(
                        this.accountStore.getByUsername(username)
                                .orElseThrow(InvalidCredentialException::new)
                );
            }

        } catch (InvalidCredentialException e) {
            return INVALID_RESULT;
        }
        return NOT_VALIDATED_RESULT;
    }

    // before return the CredentialValidationResult, check if the account is active or not
    private CredentialValidationResult validate(Account account) {
        if (!account.isActive()) {
            throw new AccountNotVerifiedException();
        }

        return new CredentialValidationResult(account.getUsername());
    }
}
