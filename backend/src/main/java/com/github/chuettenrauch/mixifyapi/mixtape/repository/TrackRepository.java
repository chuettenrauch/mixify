package com.github.chuettenrauch.mixifyapi.mixtape.repository;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Track;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends MongoRepository<Track, String> {
}
