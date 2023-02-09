package com.github.chuettenrauch.mixifyapi.mixtape_user.listener;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape_user.service.MixtapeUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MixtapeLifecycleEventListener extends AbstractMongoEventListener<Mixtape> {

    private final MixtapeUserService mixtapeUserService;

    @Override
    public void onAfterSave(AfterSaveEvent<Mixtape> event) {
        super.onAfterSave(event);

        Mixtape mixtape = event.getSource();
        this.mixtapeUserService.createIfNotExists(mixtape.getCreatedBy(), mixtape);
    }

}
