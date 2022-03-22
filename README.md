# VenuesAre

This repository contains a small implementation of the Foursquare Places API that shows a list of venues around the user’s location.

## Features

- Handle runtime permissions
- Show a list of venues based on the current location
- Filter results
- React to configuration changes

## Technical

- MVVM Architecture
- Coroutines, LiveData
- View Binding & Data Binding
- Image loading with Glide
- Dependency injection with Hilt
- Retrofit
- Fused Location Provider API

## Setup
Add your Foursquare client ID and secret to `local.gradle`. See `local.gradle.example` for details.
Tip: You can verify your credentials with `src/test/java/com/adyen/android/assignment/PlacesUnitTest.kt`
