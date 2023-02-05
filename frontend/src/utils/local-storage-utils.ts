import Mixtape from "../types/mixtape";
import localStorage from "react-secure-storage";
import TrackPerMixtape from "../types/track-per-mixtape";
import {Location} from "react-router-dom";

enum StorageKey {
    LAST_PLAY_URL = "lastPlayUrl",
    LAST_PLAYED_TRACK_PER_MIXTAPE = "lastPlayedTrackPerMixtape",
    LAST_REACHED_TRACK_PER_MIXTAPE = "lastReachedTrackPerMixtape",
}

export function updateLastPlayUrlInLocalStorage(currentLocation: Location) {
    localStorage.setItem(StorageKey.LAST_PLAY_URL, currentLocation.pathname);
}

export function getLastPlayedTrackForMixtape(mixtape: Mixtape): number {
    const lastPlayedTrackPerMixtape = localStorage.getItem(StorageKey.LAST_PLAYED_TRACK_PER_MIXTAPE) as TrackPerMixtape || {};

    return lastPlayedTrackPerMixtape[mixtape.id] ?? 0;
}

export function setLastPlayedTrackForMixtape(mixtape: Mixtape, index: number) {
    const lastPlayedTrackPerMixtape = localStorage.getItem(StorageKey.LAST_PLAYED_TRACK_PER_MIXTAPE) as TrackPerMixtape || {};

    lastPlayedTrackPerMixtape[mixtape.id] = index;
    localStorage.setItem(StorageKey.LAST_PLAYED_TRACK_PER_MIXTAPE, lastPlayedTrackPerMixtape);
}

export function getLastReachedTrackForMixtape(mixtape: Mixtape): number {
    const lastReachedTrackPerMixtape = localStorage.getItem(StorageKey.LAST_REACHED_TRACK_PER_MIXTAPE) as TrackPerMixtape || {};

    return lastReachedTrackPerMixtape[mixtape.id] ?? 0;
}

export function setLastReachedTrackForMixtape(mixtape: Mixtape, index: number) {
    const lastReachedTrackPerMixtape = localStorage.getItem(StorageKey.LAST_REACHED_TRACK_PER_MIXTAPE) as TrackPerMixtape || {};

    const lastReachedTrack = lastReachedTrackPerMixtape[mixtape.id] ?? 0;

    if (index > lastReachedTrack) {
        lastReachedTrackPerMixtape[mixtape.id] = index;
        localStorage.setItem(StorageKey.LAST_REACHED_TRACK_PER_MIXTAPE, lastReachedTrackPerMixtape);
    }
}

export default StorageKey;