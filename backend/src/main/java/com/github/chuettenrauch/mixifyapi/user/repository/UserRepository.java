package com.github.chuettenrauch.mixifyapi.user.repository;

import com.github.chuettenrauch.mixifyapi.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findBySpotifyId(String spotifyId);

}
