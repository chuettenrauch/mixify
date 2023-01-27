package com.github.chuettenrauch.mixifyapi.mixtape.repository;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MixtapeRepository extends MongoRepository<Mixtape, String> {
}
