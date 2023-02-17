namespace Form {
    export type Mixtape = {
        title: string | null,
        description: string | null,
        imageUrl: string | null,
    }

    export type Track = {
        name: string | null,
        artist: string | null,
        imageUrl: string | null,
        description: string | null,
        spotifyUri: string | null,
    }

    export type Invite = {
        mixtape: string | null,
    }
}

export default Form;