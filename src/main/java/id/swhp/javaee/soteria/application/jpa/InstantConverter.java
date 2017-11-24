package id.swhp.javaee.soteria.application.jpa;

import java.time.Instant;
import java.util.Date;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA Convention to automatically convert Instant Java 8 Time API into Database (PostgreSQL) and
 * vice versa.
 *
 * @author Sukma Wardana
 * @since 1.0
 */
@Converter(autoApply = true)
public class InstantConverter implements AttributeConverter<Instant, Date> {

    @Override
    public Date convertToDatabaseColumn(final Instant instant) {
        return (instant == null) ? null : Date.from(instant);
    }

    @Override
    public Instant convertToEntityAttribute(final Date dbData) {
        return (dbData == null) ? null : dbData.toInstant();
    }

}
