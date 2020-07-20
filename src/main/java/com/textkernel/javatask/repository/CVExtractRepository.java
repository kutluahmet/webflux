package com.textkernel.javatask.repository;

import com.textkernel.javatask.domain.document.CVExtract;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CVExtractRepository extends ReactiveMongoRepository<CVExtract, String> {
}
