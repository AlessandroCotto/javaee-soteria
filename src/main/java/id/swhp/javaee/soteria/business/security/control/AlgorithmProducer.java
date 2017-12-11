package id.swhp.javaee.soteria.business.security.control;

import id.swhp.javaee.soteria.business.security.boundary.HashGenerator;
import id.swhp.javaee.soteria.business.security.entity.HashServiceType;
import id.swhp.javaee.soteria.business.security.entity.HashType;
import id.swhp.javaee.soteria.business.security.entity.Sha;
import java.lang.annotation.Annotation;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 *
 * @author Sukma Wardana
 * @since 1.0
 */
public class AlgorithmProducer {

    @Produces
    @HashServiceType(HashType.SHA)
    @Sha
    public HashGenerator produceHashGenerator(InjectionPoint ip) {
        HashGenerator hashGenerator = null;

        for (Annotation annotation : ip.getAnnotated().getAnnotations()) {
            if (annotation instanceof Sha) {

                Sha shaAnnotation = (Sha) annotation;
                hashGenerator = new SHAGenerator(shaAnnotation.algorithm().getAlgorithmName());
            }
        }

        return hashGenerator;
    }
}
