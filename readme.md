# clij-boofCV brigde

This repository contains image format converters to bridge between [clij](https://github.com/clij) and [boofCV](https://boofcv.org/).

## Bridging between clij and boofcv

Goal of this bridge is allowing easy conversion between clij and boofcv image types. In that way developers can easily run code on the GPU using clij if speed necessary or boofcv on the CPU if a certain computer vision functionality is desired.


Conversion CPU(boofcv) -> GPU(clij) works like this:
```java
// start with a planar stack or single image
Planar<GrayU8> planarIn = //...
GrayU8 slice = //...

// convert to GPU
ClearCLBuffer buffer = clij.convert(planarIn, ClearCLBuffer.class);

// alternatively
ClearCLBuffer buffer = clij.convert(planarIn.getBand(0), ClearCLBuffer.class);

// or
ClearCLBuffer buffer = clij.convert(slice, ClearCLBuffer.class);

// show buffer using ImageJ
clij.show(buffer, "buffer");
```


Conversion GPU(clij) -> CPU(boofcv) works like this:
```java
// start with a buffer in the GPU
ClearCLBuffer bufferIn = //...

// convert from GPU to boofCV
Planar<GrayU8> input = clij.convert(bufferIn, Planar.class);

// alternatively
GrayU8 input = clij.convert(bufferIn, ImageGray.class);
```


More fully functional demo code can be found [here](https://github.com/clij/clij-boofcv/tree/master/src/test/java/net/haesleinhuepf/clij/boofcv).



## Depending on clij-boofcv

Our build system is [maven](https://maven.apache.org/). To make your project depend on clij-boofcv, just add this dependency to your maven pom.xml file:

```xml
<dependency>
  <groupId>net.haesleinhuepf</groupId>
  <artifactId>clij-boofcv</artifactId>
  <version>0.1.0</version>
</dependency>
```

To allow maven finding this artifact, add a repository to your pom.xml file:

```xml
<repository>
  <id>clij</id>
  <url>http://dl.bintray.com/haesleinhuepf/clij</url>
</repository>
```

## Feedback

Feedback is highly appreciated. Just tell me via email (rhaase@mpi-cbg.de), as [github issue](https://github.com/clij/clij-boofcv/issues) or on twitter [@haesleinhuepf](https://twitter.com/haesleinhuepf).

[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
