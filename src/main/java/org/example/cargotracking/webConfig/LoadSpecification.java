package org.example.cargotracking.webConfig;

import jakarta.persistence.criteria.Predicate;
import org.example.cargotracking.dto.LoadSearchDTO;
import org.example.cargotracking.entity.Company;
import org.example.cargotracking.entity.LoadRecord;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class LoadSpecification {

    public static Specification<LoadRecord> filterLoads(LoadSearchDTO search, Company company) {

        return (root, query, cb) -> {

            List<Predicate> predicates =
                    new ArrayList<>();

            // COMPANY FILTER

            predicates.add(

                    cb.equal(
                            root.get("company"),
                            company
                    )
            );

            // PRODUCT

            if (search.getProductName() != null &&
                    !search.getProductName().isEmpty()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("productName")),
                                "%" + search.getProductName().toLowerCase() + "%"
                        )
                );
            }

            // TRUCK

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

            // LOADED BY

            if (search.getLoadedBy() != null &&
                    !search.getLoadedBy().isEmpty()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("loadedBy")),
                                "%" + search.getLoadedBy().toLowerCase() + "%"
                        )
                );
            }

            // STATUS

            if (search.getStatus() != null) {

                predicates.add(
                        cb.equal(
                                root.get("status"),
                                search.getStatus()
                        )
                );
            }

            return cb.and(
                    predicates.toArray(
                            new Predicate[0]
                    )
            );
        };
    }
}
