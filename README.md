# images2gpx

```
images2gpx is a command line tool which creates a gpx or html files based on gps data in jpeg images.

Usage:
  java -jar images2gpx.jar [parameters]
  
parameters:(
  -i, --inputDirectory        Directory containing your images (required)
  -o, --outputDirectory       Output Directory (default: your userhome)
  -t, --outputType            supported types [gpx, google-maps-markers, google-maps-polylines] (default: gpx)
  -r, --recursive             default: false
)
  
Examples:
  java -jar images2gpx.jar -i ~/pics/2015-journeyAroundTheWorld
  java -jar images2gpx.jar -i ~/pics/2015-journeyAroundTheWorld -t google-maps-polylines -o ~/pics/2015-journeyAroundTheWorld
  java -jar images2gpx.jar -i ~/pics/all-my-journeys -t google-maps-markers --recursive
```
## build
A Java 8 Development Kit is required to build the project.

```
git clone https://github.com/polandy/images2gpx.git
cd images2gpx
./gradlew bootRepackage
```
The runnable jar is generated in build/libs
