# Google Play deployment

The `Google Play CD` workflow uses the tag suffix to select the deployment:

- `*-beta` builds, signs, and publishes an app bundle to internal testing.
- `*-prod` promotes the matching internal release to production.

It can also be run manually from the Actions tab. A manual production run
requires the existing internal `versionCode` to promote.

## GitHub environment

Create environments named `google-play-internal` and
`google-play-production`. Add protection rules if a manual approval should be
required before a deployment, especially for production.

## Google Cloud authentication

The workflow uses GitHub OIDC and Google Cloud Workload Identity Federation to
obtain short-lived credentials. It does not use a service account JSON key.

Add these repository or environment variables under Actions variables:

| Variable | Value |
| --- | --- |
| `GCP_WORKLOAD_IDENTITY_PROVIDER` | Full provider resource name, such as `projects/123456789012/locations/global/workloadIdentityPools/github-actions/providers/lottery-generator` |
| `GCP_SERVICE_ACCOUNT` | Play publishing service account email |

The Workload Identity Provider must trust this repository, and its principal
must have `roles/iam.workloadIdentityUser` on the service account. Invite the
same service account in Google Play Console and grant app-level permissions to
release to testing tracks and production.

The internal deployment builds the signed bundle before authenticating. GitHub
OIDC credentials are short-lived, so authentication is intentionally performed
immediately before the Play API upload.

## Required secrets

Add these secrets to the `google-play-internal` environment or the repository:

| Secret | Value |
| --- | --- |
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

The production environment does not need the build and signing secrets because
it promotes an existing internal release. It still resolves the two Google
Cloud variables listed above and can retain approval protection rules.

Encode files as single-line base64 values on macOS:

```bash
base64 -i app/google-services.json | tr -d '\n'
base64 -i /path/to/upload-keystore.jks | tr -d '\n'
```

Store the resulting values directly in the corresponding GitHub secrets. Never
commit the decoded files or generated Google Cloud credentials to the
repository.

## Release flow

1. Increment `versionCode` and `versionName` in `app/build.gradle.kts`.
2. Complete the normal Git Flow release into `main`.
3. Push a beta tag containing the app version and `versionCode`, for example
   `2.12.0+19-beta`. The workflow uploads versionCode 19 to internal testing.
4. After testing that release, push `2.12.0+19-prod`. The workflow extracts
   versionCode 19 from the tag and promotes that exact internal release to
   production.

Every beta upload must use a `versionCode` that has not been uploaded before.
The corresponding production tag must use the same versionCode so that the
tested artifact is promoted without rebuilding or uploading it again.
