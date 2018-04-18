# Spectralis Raw Data Import Library

This is a Java library that reads Optical Coherence Tomography (OCT) files that were created with the *Raw data export* of the SPECTRALIS viewing software of Heidelberg Engineering.
It aims to help scientists to analyze a large number of OCT scans by providing programmatically access to meta data, SLO images, scanning modes, retinal layer segmentations, and of course the scanned data itself.

Each OCT scan consists of

1. Meta information about the scan, like scanning mode, number of B-scans, dimensions, scaling, and many more. Almost all of them are exposed through the API.
2. One or many B-scans which are two-dimensional arrays that are commonly presented as gray-scale images.
3. Meta information about every B-scan with e.g. scan-coordinates or retinal layer segmentations. 
4. A Scanning Laser Ophthalmoscopy (SLO) image that gives an overview of the retina.
5. Grids with volume and thickness measurements. One of the more popular grids is the so-called ETDRS grid, a circular measurement region that was developed for a diabetes study.

This library make accessing all these information simple in only a few lines of Java, Kotlin, Mathematica, or any language that can interact with Java.
For instance, storing the SLO image as PNG can be done with the following lines

```java
HSFFile hsfFile = new HSFFile(file);
SLOImage slo = hsfFile.getSLOImage();
ImageIO.write(slo.getImage(), "png", output);
hsfFile.close();
```
![Imgur](https://i.imgur.com/OPebRMVm.png)

# Java (Kotlin) usage

Download the latest `.jar` from the release section of this repository and include it in your project.
You should consider having the sources available as well.
They contain many documentation comments and examples that show you how to use the library.
The `.jar` was compiled with Java 1.8u162 and you should use a JDK that is compatible with this version.

The library reads the data lazily which makes it fast when only certain information are accessed.
That also means, that we keep the file open until you explicitly close it.
Therefore, the general usage is (in Java)

```java
HSFFile hsfFile = new HSFFile(file);
// Do your work
hsfFile.close();
```

On creation of the `hsfFile` instance, only the file header is read which you can access by using `hsfFile.getInfo()`.
From this information, you can get all important properties of the scan and the complete API is exposed through methods of the `HSFFile` instance.

The library throws `SpectralisException` when something goes wrong during reading.
When you try to access properties that are not available for your file, the library throws a `UnsupportedVersionException`.

The [examples directory](https://github.com/halirutan/spectralis-raw-data/tree/master/src/de/halirutan/spectralis/examples) contains working code that shows how the library can be used. 
One of the examples shows how you can access the retinal layer segmentation and highlight it in the B-Scan itself

![Imgur](https://i.imgur.com/SQL0msS.png)

# Mathematica usage

For Mathematica users, the library contains an interface class `de.halirutan.spectralis.mathematica.MmaHSF` that exposes all features through static functions.
Most information are returned as `Assiciations` or as primitive tensors.
There does not exist a Mathematica package yet, but access through `JLink` is easy enough.
To work with it, you need the `.jar` and add it to the Mathematica class-path:

```mathematica
<< JLink`
AddToClassPath["/path/to/spectralis-raw-data.jar"];
```

Then, you load the interface class and make the static functions visible

```mathematica
LoadJavaClass["de.halirutan.spectralis.mathematica.MmaHSF", StaticsVisible -> True]
```

Now, you can access all functions where you use ``MmaHSF` `` as context:

```mathematica
file = "/path/to/file-with-layers.vol";
invalidEntry = MmaHSF`getInvalidFloatValue[];
info = MmaHSF`getInfo[file];
layers = MmaHSF`getLayerSegmentation[file, 50];

With[
  {
    validLayers = Select[layers, Not[MatchQ[#, {invalidEntry ..}]] &], 
    ny = info["SizeZ"], nx = info["SizeX"]
  },
  ListLinePlot[ny - Values[validLayers],
    PlotRange -> {{10, nx - 10}, {150, 300}},
    AspectRatio -> 1/3,
    PlotLegends -> Keys[validLayers]]
 ]
```

![Layers](http://i.stack.imgur.com/tOCly.png)
