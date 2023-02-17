import Track from "../types/track";
import Mixtape from "../types/mixtape";

namespace MixtapeUtils {
    export function formatCreatedAt(date: string) {
        const formattedDate: Date = new Date(date);

        return formattedDate.toLocaleDateString("en", {month: "long", year: "numeric"})
    }

    export function formatNumberOfTracks(tracks: Track[]) {
        const numText: string = tracks.length === 0 ? "No" : String(tracks.length);
        const trackText: string = tracks.length === 1 ? "track" : "tracks";

        return `${numText} ${trackText}`;
    }

    export function containsSearchTerm(mixtape: Mixtape, searchTerm: string): boolean {
        const fulltext = mixtape.title + mixtape.createdBy.name;

        return fulltext.toLowerCase().includes(searchTerm.toLowerCase());
    }
}

export default MixtapeUtils;