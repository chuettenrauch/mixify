import Invite from "../types/invite";

namespace InviteUtils {
    export function createShareLinkFromInvite(invite: Invite): string {
        return `${window.location.origin}/share/${invite.id}`
    }
}

export default InviteUtils;