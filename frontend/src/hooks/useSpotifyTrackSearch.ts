import useSpotifyApi from "./useSpotifyApi";
import {useCallback, useState} from "react";

export default function useSpotifyTrackSearch() {
    const spotifyApi = useSpotifyApi();

    const [searchTerm, setSearchTerm] = useState<string>("");
    const [searchResults, setSearchResults] = useState<Spotify.Track[]>([]);
    const [selectedSearchResult, setSelectedSearchResult] = useState<Spotify.Track | null>(null);

    const searchTracks = async (q: string) => {
        if (!spotifyApi) {
            return;
        }

        const results: Spotify.Track[] = await spotifyApi.searchTracks(q);
        setSearchResults(results);
    }

    const resetSearch = useCallback(() => {
        setSearchTerm("");
        setSearchResults([]);
        setSelectedSearchResult(null);
    }, []);

    return {
        searchTerm,
        setSearchTerm,
        searchResults,
        setSearchResults,
        selectedSearchResult,
        setSelectedSearchResult,
        searchTracks,
        resetSearch
    };
}