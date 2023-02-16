import {AuthenticatedUser} from "../types/user";
import Mixtape from "../types/mixtape";

namespace PermissionUtils {
    export function hasEditPermission(user: AuthenticatedUser | null, mixtape: Mixtape | null) {
        if (!user || !mixtape) {
            return false;
        }

        return user.id === mixtape.createdBy.id;
    }

    export function isEditable(user: AuthenticatedUser | null, mixtape: Mixtape | null) {
        return mixtape?.draft && hasEditPermission(user, mixtape);
    }
}

export default PermissionUtils;