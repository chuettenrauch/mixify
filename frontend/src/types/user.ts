type User = {
    id: string,
    name: string,
    imageUrl: string
}

export type AuthenticatedUser = User & {
    providerAccessToken: string,
    providerRefreshToken: string
}

export default User;