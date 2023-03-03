package com.github.chuettenrauch.mixifyapi.unit.mixtape_user.listener;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape_user.listener.MixtapeLifecycleEventListener;
import com.github.chuettenrauch.mixifyapi.mixtape_user.service.MixtapeUserService;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;

import static org.mockito.Mockito.*;

class MixtapeLifecycleEventListenerTest {

    @Test
    void onAfterSave_whenMixtapeSaved_thenCreateMixtapeUserEntryForCreator() {
        // given
        User creator = new User();

        Mixtape mixtape = new Mixtape();
        mixtape.setCreatedBy(creator);

        AfterSaveEvent<Mixtape> event = mock(AfterSaveEventForMixtape.class);
        when(event.getSource()).thenReturn(mixtape);

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);

        // when
        MixtapeLifecycleEventListener sut = new MixtapeLifecycleEventListener(mixtapeUserService);
        sut.onAfterSave(event);

        // then
        verify(mixtapeUserService).createIfNotExists(creator, mixtape);
    }

    @Test
    void onBeforeDelete_whenMixtapeIsDeleted_thenAllRelatedMixtapeUserAreDeletedToo() {
        // given
        ObjectId mixtapeId = new ObjectId();
        Document document = new Document("_id", mixtapeId);

        Mixtape expected = new Mixtape();
        expected.setId(mixtapeId.toString());

        BeforeDeleteEvent<Mixtape> beforeDeleteEvent = mock(BeforeDeleteEventForMixtape.class);
        when(beforeDeleteEvent.getSource()).thenReturn(document);

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);

        // when
        MixtapeLifecycleEventListener sut = new MixtapeLifecycleEventListener(mixtapeUserService);
        sut.onBeforeDelete(beforeDeleteEvent);

        // then
        verify(mixtapeUserService).deleteByMixtape(expected);
    }

    /**
     * This is only to overcome the issue, that it is not possible to mock classes with generic parameters
     */
    private static class AfterSaveEventForMixtape extends AfterSaveEvent<Mixtape> {
        public AfterSaveEventForMixtape(Mixtape source, Document document, String collectionName) {
            super(source, document, collectionName);
        }
    }

    private static class BeforeDeleteEventForMixtape extends BeforeDeleteEvent<Mixtape> {
        public BeforeDeleteEventForMixtape(Document document, Class<Mixtape> type, String collectionName) {
            super(document, type, collectionName);
        }
    }
}