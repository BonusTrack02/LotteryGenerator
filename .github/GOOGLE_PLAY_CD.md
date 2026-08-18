# Google Play internal deployment

The `Google Play CD` workflow publishes a signed app bundle to the Google Play
internal testing track when a `*-prod` tag is pushed. It can also be run
manually from the Actions tab.

## GitHub environment

Create an environment named `google-play-internal`. Add protection rules if a
manual approval should be required before each deployment.

## Required secrets

Add these secrets to the `google-play-internal` environment or the repository:

| Secret | Value |
| --- | --- |
| `PLAY_SERVICE_ACCOUNT_JSON` | Full JSON key for a Play Console service account with release permission |
| `GOOGLE_SERVICES_JSON_BASE64` | Base64-encoded production `app/google-services.json` |
| `UPLOAD_KEYSTORE_BASE64` | Base64-encoded Play upload keystore |
| `UPLOAD_STORE_PASSWORD` | Upload keystore password |
| `UPLOAD_KEY_ALIAS` | Upload key alias |
| `UPLOAD_KEY_PASSWORD` | Upload key password |
| `ADMOB_APPLICATION_ID` | Production AdMob application ID |
| `ADMOB_BANNER_AD_ID` | Production banner ad unit ID |
| `ADMOB_NATIVE_AD_ID` | Production native ad unit ID |
| `ADMOB_WEBVIEW_BANNER_AD_ID` | Production WebView banner ad unit ID |
| `ADMOB_APP_OPEN_AD_ID` | Production app-open ad unit ID |

Encode files as single-line base64 values on macOS:

```bash
base64 -i app/google-services.json | tr -d '\n'
base64 -i /path/to/upload-keystore.jks | tr -d '\n'
```

Store the resulting values directly in the corresponding GitHub secrets. Never
commit the decoded files or service-account JSON to the repository.

## Release flow

1. Increment `versionCode` and `versionName` in `app/build.gradle.kts`.
2. Complete the normal Git Flow release into `main`.
3. Tag the release using the existing format, for example
   `2.12.0+19-prod`.
4. Push the tag. The workflow builds, signs, and publishes the app bundle to
   the internal testing track.

Every Play upload must use a `versionCode` that has not been uploaded before.
