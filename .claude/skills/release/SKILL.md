---
name: release
description: Release a new version of jdbc-util to Maven Central
argument-hint: <version>
allowed-tools:
  - Bash(./gradlew clean test)
  - Bash(./gradlew publishAllPublicationsToMavenCentralRepository)
  - Bash(git tag *)
  - Bash(git log *)
  - Bash(git commit *)
  - Bash(git add *)
  - Bash(git diff *)
  - Bash(git status)
---

Release version $ARGUMENTS of jdbc-util.

Follow these steps exactly:

## 1. Validate the version argument
- The argument must be a valid semver version (e.g. `1.1.1`). Abort if missing or invalid.

## 2. Check for clean working tree
- Run `git status` and verify there are no uncommitted changes. Untracked files are OK. Abort if the tree is dirty.

## 3. Run tests
- Run `./gradlew clean test` and ensure all tests pass. Abort if any test fails.

## 4. Generate the release notes
- Run `git log` to find the previous release commit (message starts with "Release "). Note its hash.
- Collect all commits between that release and HEAD using `git log <prev-release>..HEAD`.
- Summarize the changes into a bullet-point list grouped under headings like "Bug Fixes", "Features & Improvements", etc. Only include headings that have entries.

## 5. Bump the version
- Edit `build.gradle`: change the `version = '...'` line to the new version.
- Edit `doc/SKILL.md`: update the Maven dependency version in the `implementation` example.
- Edit `README.md`: update the Maven dependency version in both the Gradle (`implementation`) and Maven XML (`<version>`) examples.

## 6. Verify version bump
- Run `git diff --name-only` and check that exactly these files were modified: `build.gradle`, `doc/SKILL.md`, `README.md`.
- If any expected file is missing from the diff, the version string was not found — abort and report which file failed.
- If unexpected files were modified, abort and report them.

## 7. Commit the release
- Stage `build.gradle`, `doc/SKILL.md`, and `README.md`.
- Commit with a message in this format (use a HEREDOC):

```
Release <version>

<release notes from step 3>
```

## 8. Tag the release
- Create an annotated git tag: `git tag -a v<version> -m "Release <version>"`

## 9. Publish to Maven Central
- Ask the user for confirmation before publishing.
- Run `./gradlew publishAllPublicationsToMavenCentralRepository`
- If this fails, inform the user but do NOT undo the commit or tag.

## 10. Summary
- Print a summary: version released, tag created, publish status.
- Remind the user to `git push && git push --tags` when ready.
