<h1 align="center">📷Weather Photo App</h1>

<p style="text-align: center">Weather Photo App is a cool and clean Android application that lets you capture photos, add weather data at your current location, and save or share them with friends! 🌤️📸📲</p>

# Table of contents
- [🌟 Features](#-features)
- [🏗️ Architecture](#-architecture)
- [📚 Libraries](#-libraries)
- [🚀 Getting started](#-getting-started)



## 🌟 Features

- Capture photo with camera using CameraX library 📷
- Get weather data at user's current location using OpenWeather API and Retrofit library 🌤️
- Use Native CPP module to demonstrate securely storing ApiKeys 🔒
- Display photo with weather data using Glide library 🖼️
- Save photo to device storage 💾
- Share photo with other apps 📲

## 🏗️ Architecture

The app is built using a clean architecture approach, with a separation of concerns into three main layers:

- Presentation layer: This layer is responsible for displaying data to the user and handling user input. It contains the Activities, Fragments, and ViewModels of the app.
- Domain layer: This layer contains the business logic of the app. It defines the use cases and interacts with the data layer to fetch and process data.
- Data layer: This layer is responsible for data storage and retrieval. It contains the repositories and data sources of the app, and interacts with external APIs and databases.
- The app uses the Hilt library for dependency injection to manage dependencies between these layers.

## 📚 Libraries

The app uses the following libraries:

- CameraX - for camera functionality 📷
- Retrofit - for network calls to OpenWeather API 🌤️
- Native CPP module - for image processing 🧑‍💻
- OpenWeather API - for weather data 🌤️
- Glide - for image loading and caching 🖼️
- Hilt - for dependency injection 🗝️


##  🚀 Getting started

To run the app, follow these simple steps:

- Clone the repository to your local machine. 👥
- Open the project in Android Studio. 🚪
- Build and run the app on an emulator or physical device. 🏃‍♀️💨
