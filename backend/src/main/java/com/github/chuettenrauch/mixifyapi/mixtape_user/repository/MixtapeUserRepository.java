package com.github.chuettenrauch.mixifyapi.mixtape_user.repository;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape_user.model.MixtapeUser;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MixtapeUserRepository extends MongoRepository<MixtapeUser, String> {

    Optional<MixtapeUser> findByUserAndMixtape(User user, Mixtape mixtape);
}
