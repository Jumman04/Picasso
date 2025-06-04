Picasso
=======

A powerful image downloading and caching library for Android

![](https://raw.githubusercontent.com/Jumman04/picasso-kotlin/refs/heads/master/website/static/sample.png)

For more information please see [the website][1]

Download
--------

## Add it in your root settings.gradle at the end of repositories:

```groovy
    dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

## Step 2. Add the dependency

```groovy
implementation 'com.github.Jumman04:picasso:51bc20abe5'
```

or Maven:

```xml

<dependency>
    <groupId>com.github.Jumman04</groupId>
    <artifactId>picasso</artifactId>
    <version>51bc20abe5</version>
</dependency>
```

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].

Picasso requires at minimum Java 8 and API 24.


ProGuard
--------

If you are using ProGuard you might need to add OkHttp's
rules: https://github.com/square/okhttp/#r8--proguard



License
--------

    Copyright 2013 Square, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: https://square.github.io/picasso/

[2]: https://search.maven.org/search?q=g:com.squareup.picasso%20AND%20a:picasso

[snap]: https://s01.oss.sonatype.org/content/repositories/snapshots/