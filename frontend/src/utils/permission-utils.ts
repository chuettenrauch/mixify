import {AuthenticatedUser} from "../types/user";
import Mixtape from "../types/mixtape";

namespace PermissionUtils {
    export function canEdit(user: AuthenticatedUser | null, mixtape: Mixtape | null) {
        if (!user || !mixtape) {
            return false;
        }

        return user.id === mixtape.createdBy.id;
    }
}

export default PermissionUtils;