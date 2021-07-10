# Rembertime download worker

## Status
 [![](https://jitpack.io/v/rembertime/download-worker-android.svg)](https://jitpack.io/#rembertime/download-worker-android) [![API](https://img.shields.io/badge/API-%2B19-brightgreen)](https://android-arsenal.com/api?level=19#l19) [![Codecov](https://codecov.io/gh/rembertime/download-worker-android/branch/develop/graph/badge.svg?token=Q1LQS6TC1E)](https://codecov.io/gh/rembertime/download-worker-android) ![Build status](https://github.com/rembertime/download-worker-android/workflows/Build%20status/badge.svg) ![Detekt](https://github.com/rembertime/download-worker-android/workflows/Detekt/badge.svg) ![Ktlint](https://github.com/rembertime/download-worker-android/workflows/Ktlint/badge.svg)

# Description
Useful library to download files asynchronously watching the progress through push notification.

## JitPack
1. Add the JitPack repository to your build file
```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
2. Add the dependency
```
dependencies {
    implementation 'com.github.rembertime:download-worker-android:1.0.0'
}
```

## Usage
All you have to do is call DownloadFileNotificationWorker and enqueue the file to download:
```
DownloadFileNotificationWorker.enqueue(context, NotificationModel(
    filePath = "https://speed.hetzner.de/1GB.bin"
))
```

You can customize your push notification by setting the following properties
```
DownloadFileNotificationWorker.enqueue(context, NotificationModel(
    filePath = "https://speed.hetzner.de/1GB.bin", // file to download
    applicationIcon = R.drawable.ic_app, // usually the app icon (1)
    customFileName = "testFile.bin", // Custom name file (2)
    customNotificationTitle = "Test notification", //  Custom title, otherwise we set file name as title (2)
    notificationIcon = R.drawable.ic_notification, // Notification icon (3)
    channelName: String = "Downloads", // Channel name for api 26 and above
    channelId: String = "default_rembertime_channel" // Channel id for api 26 and above
))
```
for more information about the channels see the [documentation](https://developer.android.com/training/notify-user/channels)

![Push notification sample](https://user-images.githubusercontent.com/29152510/125177205-36834b80-e1b0-11eb-96f3-093ad4fb7275.jpg)

## Language
By default all the wordings are in english but you can customize them! you just override defaults with your custom `string.xml`
you can see the google [documentation](https://developer.android.com/training/basics/supporting-devices/languages)
or just check the spanish example on `app` module.

## Contribute
New features, bug fixes and improvements in the translation are welcome! For questions and suggestions use the [issues](https://github.com/rembertime/download-worker-android/issues).

Before submit your PR, run the gradle checks.
```
./gradlew check
```

## Author
- Juan Manuel Fraga / juanchi.fragaa@gmail.com

## License

```
MIT License

Copyright (c) 2021 Rembertime

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```