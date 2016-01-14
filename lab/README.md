# 01-14 Lab: HTTP & Threads

For this lab, consider the provided `movies/MovieDownloader.java` class (found in the `java/src/main/java/` folder). This Java code (which is directly portable to Android) accesses the database at [omdbapi.com](http://www.omdbapi.com/), a wrapper around the IMDB API calls for getting information about movies. 

The class, when run from the command-line, asks for a movie to search for and then prints the search results (which are Strings in JSON format). You can run the program using gradle:

```
gradle run -q
```

(the `-q` runs it in "quiet mode", so you can see the UI).

### Your Primary Objective
Your task is to add comments to this code, explaining what it does and how it works. The goal is to understand the classes and methods are that are being used here (particularly `HTTPConnection`, `BufferedReader`, and `InputStream`), and demonstrate that understanding through comments. We'll utilize this code _directly_ in Android.


### Your Secondary Objective
Consider and run the class `movies/ThreadDemo.java`. (You need to modify the `build.gradle` file to specify that you want to run `ThreadDemo`, not `MovieDownloader`.

Then run the class again. Is the output the same? What is going on here?!