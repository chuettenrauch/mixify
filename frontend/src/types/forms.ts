namespace Form {
    export type Mixtape = {
        title: string,
        description: string,
        imageUrl: string,
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