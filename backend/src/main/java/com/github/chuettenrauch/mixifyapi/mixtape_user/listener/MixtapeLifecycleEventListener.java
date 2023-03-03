package com.github.chuettenrauch.mixifyapi.mixtape_user.listener;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape_user.service.MixtapeUserService;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
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

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Mixtape> event) {
        Document document = event.getSource();

        Object mixtapeId = document.get("_id");
        Mixtape mixtape = new Mixtape();
        mixtape.setId(mixtapeId.toString());

        this.mixtapeUserService.deleteByMixtape(mixtape);
    }
}
