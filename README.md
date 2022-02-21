# Music Player App
A simple music player app that loads tracks from a native local SQL Android Database, renders and plays these tracks as a background service
### Features
  * Utilizes Android MVVM native architecture to keep track of songs in a local database
  * Background services are used to play songs while the app is minimized 
  * [Umano | Android Sliding Up Panel](https://github.com/umano/AndroidSlidingUpPanel)
     * This library was used to add button functionality in a miniplayer that can be configured to fill the screen 
        
### Known Bugs
  * Loading tracks from the database crashes if iterated past a certain number 
  * Currently does not support adding new tracks except through inserting the files into the `/drawable` folder 
