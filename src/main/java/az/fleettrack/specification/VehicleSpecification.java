package az.fleettrack.specification;

import az.fleettrack.entity.Vehicle;
import az.fleettrack.enums.VehicleStatus;
import org.springframework.data.jpa.domain.Specification;

public final class VehicleSpecification {

    private VehicleSpecification() {
    }

    public static Specification<Vehicle> hasStatus(VehicleStatus status) {
        return (root, query, cb) ->
                status == null
                        ? null
                        : cb.equal(root.get("status"), status);
    }

    public static Specification<Vehicle> hasYear(Integer year) {
        return (root, query, cb) ->
                year == null
                        ? null
                        : cb.equal(root.get("year"), year);
    }

    public static Specification<Vehicle> hasAssignedDriver(Long driverId) {
        return (root, query, cb) -> {

            if (driverId == null) {
                return null;
            }

            var subquery = query.subquery(Long.class);
            var assignment = subquery.from(
                    az.fleettrack.entity.VehicleAssignment.class
            );

            subquery.select(assignment.get("id"));

            return cb.and(
                    cb.equal(
                            assignment.get("vehicle"),
                            root
                    ),
                    cb.equal(
                            assignment.get("driver").get("id"),
                            driverId
                    ),
                    cb.isNull(
                            assignment.get("unassignedAt")
                    )
            );
        };
    }
}