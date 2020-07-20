package com.textkernel.javatask.repository;

import com.textkernel.javatask.domain.document.CVExtract;
import com.textkernel.javatask.domain.document.CVProcess;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

public interface CVProcessRepository extends ReactiveMongoRepository<CVProcess, String> {}
