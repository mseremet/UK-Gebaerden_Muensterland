# UK-Gebärden Münsterland
## About this project
This is an Android app for browsing and learning the sign language "Unterstützte Kommunikation Gebärden Münsterland (UK-Gebärden Münsterland)". This sign language is meant to help children which are still learning to speak (and other handicapped people) communicate with their parents, friends and other social contacts. It consists of 210 phrases and was developed in Münster, Germany. UK-Gebärden Münsterland is available in German only.

## Features of the app
* A sign browser for all 210 phrases, including searching for and starring favorite signs
* A sign viewer displaying the videos for the phrases
* A sign trainer with two learning modes (passive and active) and learning progress tracking

## Project structure
Main entry point is the class MainActivity. Project organization and source code follow standard Android development patterns. The SQLite database containing the names of the signs and further information resides in [this location](src/database/signs.db.sqlite) and has to be zipped and copied to [this location](src/app/src/main/assets/databases/signs.db.zip) in order to be incorporated into the app. The test suite AllEspressoUITests contains all the Android Espresso UI tests.

## Repository structure
This repository is using the [Git Workflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) structure:
* A develop branch is created from master
* A release branch is created from develop
* Feature branches are created from develop
* When a feature is complete it is merged into the develop branch
* When the release branch is done it is merged into develop and master
* If an issue in master is detected a hotfix branch is created from master
* Once the hotfix is complete it is merged to both develop and master

## Supporting the development
If you want to support the development of the app, don't hesitate to get in contact.

## Licenses
Please note that there are different licenses for the source code of the app, the videos and the logo of the app.
