package com.github.chuettenrauch.mixifyapi.mixtape.repository;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MixtapeRepository extends MongoRepository<Mixtape, String> {

    boolean existsByIdAndCreatedByAndDraftTrue(String id, User createdBy);
}
