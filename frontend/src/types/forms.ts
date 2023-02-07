namespace Form {
    export type Mixtape = {
        title: string | null,
        description: string | null,
        imageUrl: string | null,
    }

    export type Track = {
        name: string,
        artist: string,
        imageUrl: string,
        description: string,
        providerUri: string,
    }
}

export default Form;