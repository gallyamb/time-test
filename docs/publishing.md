# Publishing

Build and test on Java 17 without credentials:

```sh
./gradlew clean build
```

The version defaults to `gradle.properties`. Supply `-PreleaseVersion=0.1.1` to
build a fixed release candidate. All five libraries and the `platform` BOM share
that version and group `io.github.gallyamb.time`.

Prepare a local Maven repository (does not upload anything):

```sh
./gradlew clean build publishAllPublicationsToLocalStagingRepository -PreleaseVersion=0.1.1
./gradlew -p verification/consumer run -PtimeTestVersion=0.1.1 -PtimeTestRepository="$PWD/build/staging-repository"
```

Inspect `build/staging-repository`: each Java publication contains the binary,
sources, javadoc, POM and Gradle metadata; the platform has its POM and metadata.
For a signed local rehearsal, supply `-PsignArtifacts=true` and the signing
properties below. Unsigned local artifacts do not prove that signing works.

## Central Portal release

Publishing uses `com.vanniktech.maven.publish.base` 0.34.0, compatible with the
existing Gradle 8.5 wrapper. It uses Central Portal, not the retired OSSRH endpoint.
Register or migrate the `io.github.gallyamb` namespace at Central Portal and make
the public signing key discoverable before publishing.

Configure the GitHub `production` environment with these secrets:

- `MAVEN_CENTRAL_USERNAME`: Portal user-token username (not the account login).
- `MAVEN_CENTRAL_PASSWORD`: Portal user-token password.
- `GPG_SECRET_KEY_CONTENT`: ASCII-armored private signing key.
- `SIGNING_KEYID`: signing key ID (optional for a complete in-memory key).
- `SIGNING_PASSWORD`: key passphrase, if any.

The workflow prefers the complete `MAVEN_CENTRAL_*` pair. If neither is set,
it accepts `OSSRH_USERNAME` and `OSSRH_PASSWORD` as aliases for an existing Portal
token. It never mixes credentials from the two pairs. An old OSSRH-issued token
is not a Portal token: renaming it will not make it valid. Generate a Portal token
at <https://central.sonatype.com/usertoken> if needed; see the
[Sonatype authentication requirements](https://central.sonatype.org/publish/publish-portal-ossrh-staging-api/#authentication).
Existing signing secrets can be reused with a valid signing key whose public
part is available for signature verification.

The workflow checks out the release tag and builds/tests without credentials.
Only its publishing step receives secrets. Create a matching tag such as
`v0.1.1`, or manually dispatch the publishing workflow with an existing tag.
The same version is passed to every publication, including the BOM.

For a local release, use `RELEASE_TAG=v0.1.1` and Gradle properties supplied via
environment variables prefixed with `ORG_GRADLE_PROJECT_`: `mavenCentralUsername`,
`mavenCentralPassword`, `signingInMemoryKey`, optional `signingInMemoryKeyId` and
`signingInMemoryKeyPassword`. Then run:

```sh
./gradlew publishAndReleaseToMavenCentral -Prelease=true -PreleaseVersion=0.1.1
```

After Portal reports publication and Central has synchronized, test a clean
consumer with an empty Gradle user home and no local repository override:

```sh
GRADLE_USER_HOME="$(mktemp -d)" ./gradlew -p verification/consumer run -PtimeTestVersion=0.1.1
```

This consumer uses Maven Central only unless `timeTestRepository` is explicitly
provided. It has no project dependencies, composite builds or `mavenLocal()`.
Do not mark a release complete until this check succeeds.
