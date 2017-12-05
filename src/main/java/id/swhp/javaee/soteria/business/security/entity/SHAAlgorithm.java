package id.swhp.javaee.soteria.business.security.entity;

/**
 *
 * @author Sukma Wardana
 * @since 1.0
 */
public enum SHAAlgorithm {
    SHA256("SHA-256"),
    SHA512("SHA-512");

    private final String name;

    private SHAAlgorithm(String name) {
        this.name = name;
    }

    public String getAlgorithmName() {
        return this.name;
    }
}
