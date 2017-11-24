package id.swhp.javaee.soteria.business.security.control;

import id.swhp.javaee.soteria.business.security.boundary.HashGenerator;
import id.swhp.javaee.soteria.business.security.entity.HashAlgorithm;
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
    @HashAlgorithm
    public HashGenerator produceHashGenerator(InjectionPoint ip) {

        Annotated annotated = ip.getAnnotated();

        HashAlgorithm hashAlgorithm = annotated.getAnnotation(HashAlgorithm.class);

        HashGenerator hashGenerator = new HashGenerator(hashAlgorithm.algorithm().getAlgorithmName());

        return hashGenerator;
    }
}
