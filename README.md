# TvMazeClient

![](https://img.shields.io/badge/version-v1.0.0-blue)  ![](https://img.shields.io/badge/platform-android-red)

## Welcome
Tv Maze Client is an app to search and get information about produced tv series.

The app was created to an assessment process.

## Setup

The app supports Light and Dark mode.

### Software Setup
-  [Android Studio Bumblebee | 2021.1.1 Patch 1](https://developer.android.com/studio?gclid=Cj0KCQjw8amWBhCYARIsADqZJoVZuFFOp4_Y2zww_wY8fyKNk2nwKYNJ23QFmrSvDv7-wTR0G_xxLFYaAlPgEALw_wcB&gclsrc=aw.ds "Android Studio")

### API Reference

- [TV Maze](https://www.tvmaze.com/api "TV Maze")

### Language

- Kotlin

### Architecture

- MVVM (Model View ViewModel)

### Dependency Injection

- [Koin](https://insert-koin.io/ "Koin")

### Multithreading

- [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines?gclid=CjwKCAjw7rWKBhAtEiwAJ3CWLKmcgJFMZLK1QyQwWwfd5_Oy7Da_YNByntiMwhAcQxbpwAbj9fqIORoCIWEQAvD_BwE&gclsrc=aw.ds "Coroutine")
- [LiveDatas](https://developer.android.com/topic/libraries/architecture/livedata?hl=pt-br "LiveDatas")  

### HTTP Client

- [Retrofit](https://square.github.io/retrofit/ "Retrofit")

### Database

- [Room](https://developer.android.com/training/data-storage/room "Room")

### Other Tools

- [Glide](http://bumptech.github.io/glide/ "Glide") - load remote images.
- [Gson](https://github.com/google/gson "Gson") - JSON serialization.
- [Material Design](https://material.io/develop/android/docs/getting-started "Material Design") - UI tools.
- [Lottie](https://airbnb.io/lottie/#/ "Lottie") - MainScreen Animation

## Packages Structure
- 📔 **api** (Store the api methods)
- 📔 **data** (Store the data objects)
- 📔 **di** (Store the dependecy injection setup files)
- 📔 **extensions** (Store the app extensions)
- 📔 **repositories** (Store the repositories interfaces to call the api services)
- 📔 **room** (Store the classes to setup and access the Room database)
- 📔 **session** (Store the classes to setup the shared preferences)
- 📔 **ui** (Store the UI files, like Activities, Fragments, Adapters and the ViewModels)
- 📔 **util** (Store the util classes)
- 📔 **widget** (Store the custom widgets)

## Assessment Requirements

- ✅ List all of the series contained in the API used by the paging scheme provided by the API.
- ✅ Allow users to search series by name.
- ✅ The listing and search views must show at least the name and poster image of the series.
- ✅ After clicking on a series, the application should show the details of the series, showing the following information: Name, Poster, Days and time during which the series airs, Genres, Summary, List of episodes separated by season
- ✅ After clicking on an episode, the application should show the episode’s information, including: Name, Number, Season, Summary, Image, if there is one
      
#### Bonus

- ✅ Allow the user to set a PIN number to secure the application and prevent unauthorized users.
- ✅ For supported phones, the user must be able to choose if they want to enable fingerprint authentication to avoid typing the PIN number while opening the app.
- ✅ Allow the user to save a series as a favorite.
- ✅ Allow the user to delete a series from the favorites list.
- ✅ Allow the user to browse their favorite series in alphabetical order, and click on one to see its details.
- ✅ Create a people search by listing the name and image of the person.
- ✅ After clicking on a person, the application should show the details of that person, such as: Name, Image, Series they have participated in, with a link to the series details.

**All requirements have been made in the app**

## Screenshots
![Screenshot_20220719-135039_TV Maze Client](https://user-images.githubusercontent.com/4115436/179810157-725c61dd-4239-4bd5-9c14-2176598f926a.jpg)
![Screenshot_20220719-135111_TV Maze Client](https://user-images.githubusercontent.com/4115436/179810176-98d25f2c-0e2f-429b-9858-cb5a0c8404d8.jpg)
![Screenshot_20220719-135124_TV Maze Client](https://user-images.githubusercontent.com/4115436/179810191-de723ecb-a753-49fc-b75a-38a3f1ff3c64.jpg)
![Screenshot_20220719-135150_TV Maze Client](https://user-images.githubusercontent.com/4115436/179810201-e7c4d986-1ac7-4c58-b106-5cc0e1d37983.jpg)
![Screenshot_20220719-135206_TV Maze Client](https://user-images.githubusercontent.com/4115436/179810215-9b3365ef-c536-44d2-90a8-7a1f8fef3471.jpg)
![Screenshot_20220719-135218_TV Maze Client](https://user-images.githubusercontent.com/4115436/179810228-16ad8f0c-64aa-45fd-996b-c21075f9a651.jpg)
![Screenshot_20220719-135231_TV Maze Client](https://user-images.githubusercontent.com/4115436/179810242-97c0351c-e1b2-47b2-b01f-e5c92a4dbe56.jpg)
![Screenshot_20220719-135243_TV Maze Client](https://user-images.githubusercontent.com/4115436/179810250-20ba9c26-558a-405b-82ed-81c6fe102655.jpg)
![Screenshot_20220719-135313_TV Maze Client](https://user-images.githubusercontent.com/4115436/179810257-032cdc73-d627-4830-9890-fa20aa16f9da.jpg)
