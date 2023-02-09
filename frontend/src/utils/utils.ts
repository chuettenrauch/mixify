namespace Utils {
    export function createLoginLink(): string {
        return `/login?redirect_url=${encodeURIComponent(window.location.href)}`;
    }

    export function createOAuth2AuthorizationLink(searchParams: URLSearchParams): string {
        const apiBaseUrl = process.env.REACT_APP_API_BASE_URL ?? window.location.origin;
        const authorizationUrl = new URL(`${apiBaseUrl}/oauth2/authorization/spotify`);

        searchParams.forEach((value, key) => authorizationUrl.searchParams.append(key, value));

        return authorizationUrl.toString();
    }
}

export default Utils;