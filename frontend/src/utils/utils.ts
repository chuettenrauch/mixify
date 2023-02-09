namespace Utils {
    const defaultAuthorizeUrl = `${process.env.REACT_APP_API_BASE_URL ?? ""}/oauth2/authorization/spotify`;

    export function createLoginLink(): string {
        return `/login?redirect_url=${encodeURIComponent(window.location.href)}`;
    }

    export function createOAuth2AuthorizationLink(searchParams: URLSearchParams): string {
        const authorizationUrl = new URL(defaultAuthorizeUrl);

        searchParams.forEach((value, key) => authorizationUrl.searchParams.append(key, value));

        return authorizationUrl.toString();
    }
}

export default Utils;