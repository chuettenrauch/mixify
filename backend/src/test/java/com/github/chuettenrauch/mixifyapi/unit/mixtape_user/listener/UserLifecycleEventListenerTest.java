package com.github.chuettenrauch.mixifyapi.unit.mixtape_user.listener;

import com.github.chuettenrauch.mixifyapi.mixtape_user.listener.UserLifecycleEventListener;
import com.github.chuettenrauch.mixifyapi.mixtape_user.service.MixtapeUserService;
import com.github.chuettenrauch.mixifyapi.user.model.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;

import static org.mockito.Mockito.*;

class UserLifecycleEventListenerTest {

    @Test
    void onBeforeDelete_whenUserIsDeleted_thenAllRelatedMixtapeUserAreDeletedToo() {
        // given
        ObjectId userId = new ObjectId();
        Document document = new Document("_id", userId);

        User expected = new User();
        expected.setId(userId.toString());

        BeforeDeleteEvent<User> beforeDeleteEvent = mock(BeforeDeleteEventForUser.class);
        when(beforeDeleteEvent.getSource()).thenReturn(document);

        MixtapeUserService mixtapeUserService = mock(MixtapeUserService.class);

        // when
        UserLifecycleEventListener sut = new UserLifecycleEventListener(mixtapeUserService);
        sut.onBeforeDelete(beforeDeleteEvent);

        // then
        verify(mixtapeUserService).deleteByUser(expected);
    }

    /**
     * This is only to overcome the issue, that it is not possible to mock classes with generic parameters
     */
    private static class BeforeDeleteEventForUser extends BeforeDeleteEvent<User> {
        public BeforeDeleteEventForUser(Document document, Class<User> type, String collectionName) {
            super(document, type, collectionName);
        }
    }
}