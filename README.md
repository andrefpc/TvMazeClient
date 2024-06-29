# TvMazeClient

![](https://img.shields.io/badge/version-v1.1.0-blue)  ![](https://img.shields.io/badge/platform-android-red)

## Welcome
Tv Maze Client is a comprehensive app designed to search for and provide detailed information about TV series.

Originally, this app utilized XML for view creation and Koin for dependency injection. To enhance and modernize the app, I have recreated the views using Jetpack Compose and switched the dependency injection framework to Hilt. Rather than replacing the original implementation, I have maintained both versions, allowing users to choose and evaluate their preferred approach.

## Setup

The app supports Light and Dark mode.

### Software Setup
-  [Android Studio Jellyfish | 2023.3.1](https://developer.android.com/studio?gad_source=1&gclid=CjwKCAjw4f6zBhBVEiwATEHFVmpf2NLDn7JXHQBQRuBgbBOdqonr5bdd5wYU2pK2Ek24D8HLCu7-yxoCD7MQAvD_BwE&gclsrc=aw.ds)

### Target SDK

### API Reference

- [TV Maze](https://www.tvmaze.com/api "TV Maze")

### Language

- Kotlin

### Architecture

- MVVM (Model View ViewModel)

### UI Components

- XML
- Jetpack Compose

### Dependency Injection

- [Koin](https://insert-koin.io/ "Koin") (For XML-Based Path)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) (For Jetpack Compose Path)

### Multithreading

- [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines?gclid=CjwKCAjw7rWKBhAtEiwAJ3CWLKmcgJFMZLK1QyQwWwfd5_Oy7Da_YNByntiMwhAcQxbpwAbj9fqIORoCIWEQAvD_BwE&gclsrc=aw.ds "Coroutine")

### Observables
- [LiveDatas](https://developer.android.com/topic/libraries/architecture/livedata?hl=pt-br "LiveDatas") (For XML-Based Path)
- [StateFlows and ShareFlows](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow) (For Jetpack Compose Path)

### HTTP Client

- [Retrofit](https://square.github.io/retrofit/ "Retrofit")

### Database

- [Room](https://developer.android.com/training/data-storage/room "Room")

### Remote Images Load
- [Glide](http://bumptech.github.io/glide/ "Glide") (For XML-Based Path) 
- [Coil](https://github.com/coil-kt/coil) (For Jetpack Compose Path)

### Shimmer Loading
- [Facebook Shimmer](https://github.com/facebookarchive/shimmer-android?tab=readme-ov-file) (For XML-Based Path) 
- [Compose Shimmer](https://github.com/valentinilk/compose-shimmer) (For Jetpack Compose Path)

### Other Tools

- [Gson](https://github.com/google/gson "Gson") - JSON serialization.
- [Material Design](https://material.io/develop/android/docs/getting-started "Material Design") - UI tools.
- [Lottie](https://airbnb.io/lottie/#/ "Lottie") - MainScreen Animation

## Packages Structure
- 📔 **api** (Store the api methods)
- 📔 **core** (Store the core classes that will be used through the code)
  - 📔 **data** (Store data objects)
  - 📔 **domain** (Store repositories, use cases and business logics)
    - 📔 **exception** (Store the exceptions created to handle errors)
    - 📔 **repository** (Store the repositories interfaces to call the api services)
    - 📔 **room** (Store the classes to setup and access the Room database)
    - 📔 **session** (Store the classes to setup the shared preferences)
    - 📔 **use_case** (Store the classes that handle business logics)
  - 📔 **extensions** (Store the app extensions)
  - 📔 **util** (Store the util classes)
- 📔 **di** (Store the dependecy injection setup files)
- 📔 **ui** (Store the UI files, like Activities, Fragments, Adapters and the ViewModels)
  - 📔 **compose** (Store the UI files for the Jetpack Compose perspective of the app)
    - 📔 **widget** (Store the custom widgets)
  - 📔 **xml_bases** (Store the UI files for the XML-based perspective of the app)
    - 📔 **widget** (Store the custom widgets)
   
## Unit Tests

111 Unit Tests were created for this application and they are separated into repository tests, use case tests, and view model tests.

#### Tests Packages Structure
- 📔 **repository** (Store Repository tests)
- 📔 **use_case** (Store Use Case tests)
- 📔 **viwe_model** (Store ViewModel tests)
- 📔 **util** (Store Mocks to be used in the tests)
  
<img width="1378" alt="Screenshot 2024-06-29 at 17 15 28" src="https://github.com/andrefpc/TvMazeClient/assets/4115436/48386491-d021-42f4-8d21-c58830ebae60">


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
- ✅ Create Unit Tests

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
