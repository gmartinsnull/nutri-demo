# Nutri Demo

This Android application displays a list of foods which are retrieved either from local database or
Nutritionix API. Each item in the list represents compiled data from the aforementioned API, containing
attributes such as name, calories and photo. The goal of this project is to display how an android
architecture should look like in regards to performance, scalability and code cleanliness/readability.

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Install](#install)
- [Focus and Trade-offs](#focus-and-trade-offs)
- [Libraries](#libraries)
- [Outro](#outro)

## Features
- Food search: Retrieves a list of foods based on the text input.  If the data does not 
  exist in the database, the application fetches it from the API, otherwise, it retrieves from the
  database. If no text is in the search field, it retrieves all foods saved in the database upon
  "done" keyboard button interaction.
- Food list: Displays a list of foods sorted by name. All data is retrieved from the local database 
  after being fetched from Nutritionix API.
- Food info screen: Upon selecting a food item, user are taken to another screen where all nutrition values as well as recipe ingredients 
  are displayed.
- Food info nearby places: A list of places are displayed in a map view within a 1500m radius.
- Food info ingredient change: Users are able to change the ingredient amount values. The application
  recalculates all nutritional values accordingly.

## Architecture
The architecture pattern applied in this project was a combination of CLEAN architecture and MVVM. The
project structure is organized into layers according to CLEAN architecture pattern whereas the implementation
falls into Model-View-ViewModel pattern.

## Install
### GitHub
Since it's a public repository, it can simply be cloned from develop branch into your local directory
using Git. Once cloned, Open Android Studio, on the welcome screen click on "Open" button. Select the
directory where the project was cloned to.

### Zip
Unzip the file in a directory (ideally in the designated android studio folder: AndroidStudioProjects).
Open Android Studio and, on the welcome screen click on "Open" button. Select the directory where the project
was unzipped to.

## Focus and Trade-offs

### Focus
The primary focus of this project was the base architecture, data layer and the main Jetpack Compose
elements. With a solid foundation, applications are more likely to be scalable and maintainable in the
foreseeable future.

### Trade-offs
Some trade-offs were in the realm of UX/UI and unit testing. Priorities were shifted in order to 
optimize architecture and data layer over better UX/UI such as animations/transitions, a more polished
and detailed Food Info screen for each food and overall material design practices such as theming and 
typography.

# Libraries
The following are the primary third party libraries used in this project:
- Hilt
- Jetpack Compose
- Coroutines
- Retrofit2
- Moshi
- OkHttp
- Room
- Mockito
- Coil

# Outro
I'd like to share the resources I used during the development of this project:
- StackOverflow
- Medium articles
- Material3 Components, theming and typography (https://m3.material.io)
- Material theme builder (https://material-foundation.github.io/material-theme-builder/)
- Android docs (https://developer.android.com/)
- Kotlin docs (https://kotlinlang.org)
- ChatGPT