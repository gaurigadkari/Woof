# Woof
An Android app that displays images for different dog breeds using MVVM architectural components and RxJava

## User Stories

The following **required** functionality is completed:

* [x] The need to reload the data from the network on orientation change is eliminated
* [x] AsyncTask is replaced with RxJava2 for handling the threading

The following **additional** features are implemented:

* [x] Added an app icon
* [x] Added a placeholder image
* [x] Used MVVM architectural components
* [x] Caching the observables in ViewModel
* [x] Replaced Picasso with Glide
* [x] Fixed lint issues
* [x] To optimize performance, observable is returning a single Breed object at a time instead of the entire list
* [x] Improved the user interface through styling and coloring.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

![Video Walkthrough](woof.gif)
