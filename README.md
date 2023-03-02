# Mixify

Capstone project for [neue fische's Java Development Bootcamp](https://www.neuefische.de/bootcamp/java-development).

## About
Mixify is an app, that brings back the good, old days of cassette tapes. Users can create mixtapes, share them with friends and listen to them directly in the app. The chosen tracks will be revealed one after another - skipping is not allowed. (*spotify premium account & explicit authorization through the developer required)

## Development

### Requirements

- Register to [Spotify for Developer](https://developer.spotify.com/dashboard)
- Create an app
- Set the following URI under `Edit settings -> Redirect URIs`: `http://localhost:8080/oauth2/code/spotify`
- add your spotify user to `Users and Access`
- find `Client ID` and `Client Secret` and set them as environment variables in your `~/.profile`:
```sh
cat <<EOT >> ~/.profile

# Mixify env vars
export SPOTIFY_CLIENT_ID=<CLIENT_ID>
export SPOTIFY_CLIENT_SECRET=<CLIENT_SECRET>
EOT
```

### Start

- Run to start the app:
```sh
docker compose up -d
```

- the React App is then available under `http://localhost:3000`
- the API is then available under `http://localhost:8080/api`