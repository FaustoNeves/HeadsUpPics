# HeadsUpPics

App for Android written in Kotlin! Its main job is to acts as a backup for your photos. You can upload your photos in google cloud and download it (or exclude it) anytime :)

- [Firebase](https://firebase.google.com/?hl=en[)
    - [Getting started with Firebase](https://firebase.google.com/docs/remote-config/get-started?hl=en-us&platform=android)
    - [Firebase Authentication](https://firebase.google.com/products/auth?hl=en) is very easy to use
    - [Cloud Storage for Database](https://firebase.google.com/products/storage?hl=en)
    - [Pricing plans](https://firebase.google.com/pricing?hl=en)
- [Recycler View](https://developer.android.com/guide/topics/ui/layout/recyclerview?hl=en-us) for displaying the images. One of the biggest challenges is to keep the recycler view updated when the items are manipulated
- [Download Manager](https://developer.android.com/reference/android/app/DownloadManager) to fetch data directly to your download folders

This app was an exercise for me to experiment Firebase. The idea is to upload data to a cloud bucket to fetch it later and to display a fresh list of this data. Expect lots of toasts and NO proper architecting
