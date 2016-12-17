# images2gpx

```
images2gpx is a command line tool which creates a gpx file based on gps data in jpeg images.

Usage:
  java -jar images2gpx.jar [parameters]
  
  Example:
  java -jar images2gpx.jar -i ~/pics/2015-journeyAroundTheWorld

parameters:(
  -i, --inputDirectory        Directory containing your images (required)
  -o, --outputDirectory       Output Directory (default: your userhome)
  -t, --outputType            supported types [gpx] (default: gpx)
)
```
## build
A Java 8 Development Kit is required to build the project.

```
git clone https://github.com/polandy/images2gpx.git
cd images2gpx
./gradlew bootRepackage
```
The runnable jar is generated in build/libs
