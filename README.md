# Exactitude - Converter and Conversion comparisons
This project is used to convert files from one format to another, and to provide a measurement of the quality of the output. It uses the concept of [perceptual hashing](https://en.wikipedia.org/wiki/Perceptual_hashing "Wikipedia article") to create a human-like visual comparison of the original and source files.

<p align="center">
<img src="/Exactitude.jpg" alt="Exactitude image of an eye">
</p>

## Dependencies
Exactitude depends on a number of external software packages which are all automatically created by the build scripts:
* Perceptual hashing library [pHash](http://www.phash.org/)
* Java Advanced Imaging library [jai](http://www.oracle.com/technetwork/articles/javaee/jai-142803.html)
* Independent JPEG Group library [jpegsrc](http://www.ijg.org/)
* Audio and video streaming library [ffmpeg](https://ffmpeg.org/)
* Audio file read/write library [libsndfile](http://www.mega-nerd.com/libsndfile/)
* Audio sample rate converter library [libsamplerate](http://www.mega-nerd.com/SRC/) 
* MPEG audio decoder [mpg123](https://www.mpg123.de/)
* Image processing library [CImg](http://cimg.eu/)

