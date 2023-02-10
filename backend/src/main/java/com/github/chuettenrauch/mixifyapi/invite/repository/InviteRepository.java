package com.github.chuettenrauch.mixifyapi.invite.repository;

import com.github.chuettenrauch.mixifyapi.invite.model.Invite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteRepository extends MongoRepository<Invite, String> {
}
