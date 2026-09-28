package az.fleettrack.specification;

import az.fleettrack.entity.Driver;
import org.springframework.data.jpa.domain.Specification;

public final class DriverSpecification {

    private DriverSpecification() {
    }

    public static Specification<Driver> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) ->
                firstName == null || firstName.isBlank()
                        ? null
                        : criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get("firstName")
                        ),
                        "%" + firstName.toLowerCase() + "%"
                );
    }

    public static Specification<Driver> hasLastName(String lastName) {
        return (root, query, criteriaBuilder) ->
                lastName == null || lastName.isBlank()
                        ? null
                        : criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get("lastName")
                        ),
                        "%" + lastName.toLowerCase() + "%"
                );
    }

    public static Specification<Driver> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
                email == null || email.isBlank()
                        ? null
                        : criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get("email")
                        ),
                        "%" + email.toLowerCase() + "%"
                );
    }
}