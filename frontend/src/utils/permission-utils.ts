import {AuthenticatedUser} from "../types/user";
import Mixtape from "../types/mixtape";

namespace PermissionUtils {
    function isCreator(user: AuthenticatedUser | null, mixtape: Mixtape | null) {
        if (!user || !mixtape) {
            return false;
        }

        return user.id === mixtape.createdBy.id;
    }

    export function isEditable(user: AuthenticatedUser | null, mixtape: Mixtape | null) {
        return mixtape?.draft && isCreator(user, mixtape);
    }

    export function isShareable(user: AuthenticatedUser | null, mixtape: Mixtape | null) {
        return !mixtape?.draft && isCreator(user, mixtape);
    }
}

export default PermissionUtils;