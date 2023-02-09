package com.github.chuettenrauch.mixifyapi.mixtapeUser.repository;

import com.github.chuettenrauch.mixifyapi.mixtapeUser.model.MixtapeUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MixtapeUserRepository extends MongoRepository<MixtapeUser, String> {
}
