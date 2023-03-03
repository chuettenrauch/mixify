package com.github.chuettenrauch.mixifyapi.mixtape_user.listener;

import com.github.chuettenrauch.mixifyapi.mixtape_user.service.MixtapeUserService;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserLifecycleEventListener extends AbstractMongoEventListener<User> {

    private final MixtapeUserService mixtapeUserService;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<User> event) {
        Document document = event.getSource();

        Object userId = document.get("_id");
        User user = new User();
        user.setId(userId.toString());

        this.mixtapeUserService.deleteByUser(user);
    }


}
