# QualityFPV
Independent Study project for my senior year of my CS degree
![Banner](previews/QualityFpvBanner.png)

## Description
This app is for all the fpv enthusiasts from beginner to simply pilots that just want to keep up to date on the best products. Users
can select one of the three major drone websites to list the top drone products by category all in one place so there is never any question
of if you know what's the best buy.

## Tools & Open Source Libraries
* [Kotlin](https://kotlinlang.org/) - Language used for this app
* [JSOUP](https://jsoup.org/) - Handles scraping/parsing of websites
* [Room](https://developer.android.com/jetpack/androidx/releases/room) - Stores all product data into local device database
* [Live Data](https://developer.android.com/topic/libraries/architecture/livedata) - Handles observable data
* [View Model](https://developer.android.com/topic/libraries/architecture/viewmodel) - Handles data surviving orientation changes
* [Anko Coroutines](https://github.com/Kotlin/anko) - Handles asynchronous network calls
* [Glide](https://github.com/bumptech/glide) - Handles loading and displaying images
* [Android YouTube Player](https://github.com/PierfrancescoSoffritti/android-youtube-player) - Handles loading and displaying youtube videos in player objects
* [YouTube API](https://developers.google.com/youtube/v3) - Handles searches for youtube videos

## Features

### Biggest FPV Websites
Select top products between the biggest fpv retailers!

![Site Selection](previews/site_select_demo.gif)

### Top Products by Category
View top rated products by category from each retailer.

![Site Selection](previews/categorize_demo.gif)

### View Product Details
View product images, description, reviews, and related videos.

![Site Selection](previews/product_details_demo.gif)

### The Ideal Drone
View the top rated item from each category of every website to build the most ideal drone!

![Site Selection](previews/ideal_drone_demo.gif)
