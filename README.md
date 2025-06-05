Picasso
=======

A powerful image downloading and caching library for Android

![Sample](https://raw.githubusercontent.com/Jumman04/picasso-kotlin/refs/heads/master/website/static/sample.png)

For more information, please see [the website][1].

Download
--------

### Step 1. Add this to your `settings.gradle` (or `settings.gradle.kts`):

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
````

### Step 2. Add the dependency

**Gradle:**

```groovy
implementation 'com.github.Jumman04:picasso:51bc20abe5'
```

> Snapshots of the development version are available in [Sonatype's snapshots repository][snap].

⚙️ **Picasso requires Java 8+ and Android API 24+.**

---

## ProGuard

If you are using ProGuard, you might need to add OkHttp's rules:
👉 [https://github.com/square/okhttp/#r8--proguard](https://github.com/square/okhttp/#r8--proguard)

---

## ❤️ About This Fork

This is a **community-maintained fork** of the original [Picasso](https://github.com/square/picasso)
library by [Square, Inc.](https://squareup.com/).

> All credit and appreciation go to the original creators —
> we just removed deprecated Android APIs to make the library compatible with the latest Android
> versions (API 24 to 36+).

📌 **Note:** No core logic has been modified.
This fork simply updates deprecated methods to keep Picasso alive and usable in modern Android
projects.

---

[1]: https://square.github.io/picasso/

[2]: https://search.maven.org/search?q=g:com.squareup.picasso%20AND%20a:picasso

[snap]: https://s01.oss.sonatype.org/content/repositories/snapshots/