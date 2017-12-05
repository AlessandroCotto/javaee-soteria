package id.swhp.javaee.soteria.business.security.control;

import id.swhp.javaee.soteria.business.security.boundary.HashGenerator;
import id.swhp.javaee.soteria.business.security.entity.Sha;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 *
 * @author Sukma Wardana
 * @since 1.0
 */
public class AlgorithmProducer {

    @Produces
    @Sha
    public HashGenerator produceHashGenerator(InjectionPoint ip) {

        Annotated annotated = ip.getAnnotated();

        Sha hashAlgorithm = annotated.getAnnotation(Sha.class);

        HashGenerator hashGenerator = new SHAGenerator(hashAlgorithm.algorithm().getAlgorithmName());

        return hashGenerator;
    }
}
