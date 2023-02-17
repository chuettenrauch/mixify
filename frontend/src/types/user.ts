type User = {
    id: string,
    name: string,
    imageUrl: string
}

export type AuthenticatedUser = User & {
    accessToken: string,
    refreshToken: string
}

export default User;