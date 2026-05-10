package org.example.cargotracking.webConfig;

import jakarta.persistence.criteria.Predicate;
import org.example.cargotracking.dto.LoadSearchDTO;
import org.example.cargotracking.entity.LoadRecord;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class LoadSpecification {

    public static Specification<LoadRecord> filterLoads(
            LoadSearchDTO search
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates =
                    new ArrayList<>();

            if (search.getProductName() != null &&
                    !search.getProductName().isEmpty()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("productName")),
                                "%" + search.getProductName().toLowerCase() + "%"
                        )
                );
            }

            if (search.getTruckNumber() != null &&
                    !search.getTruckNumber().isEmpty()) {

                predicates.add(
                        cb.like(
                                cb.lower(
                                        root.get("truck")
                                                .get("truckNumber")
                                ),
                                "%" + search.getTruckNumber().toLowerCase() + "%"
                        )
                );
            }

            if (search.getLoadedBy() != null &&
                    !search.getLoadedBy().isEmpty()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("loadedBy")),
                                "%" + search.getLoadedBy().toLowerCase() + "%"
                        )
                );
            }

            if (search.getStatus() != null) {

                predicates.add(
                        cb.equal(
                                root.get("status"),
                                search.getStatus()
                        )
                );
            }

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}
