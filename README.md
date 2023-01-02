# Weather Clean Architecture [![Codacy Badge](https://app.codacy.com/project/badge/Grade/404946db725448b89bcee7807e96a92f)](https://www.codacy.com/gh/hungnd-vnse/Weather-Clean-Architecture/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=hungnd-vnse/Weather-Clean-Architecture&amp;utm_campaign=Badge_Grade)

A weather android app demonstrates Clean Architecture and is written in Kotlin.

## Introduction

I try to follow Clean Architecture guide recommended architecture for building robust, high-quality
apps.

  * User Interface built with XML
  * A single-activity architecture
  * A **presentation layer** that contains a main screen and a **ViewModel** per screen (or
  feature).
  * Reactive UIs using **Flow** and **Coroutines** for asynchronous operations.
  * A **data layer** with repositories and data sources (local using Room and a api service).
  * Dependency injection
    using [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
  * I will add **domain layer** soon