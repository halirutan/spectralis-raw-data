# Spectralis Raw Data Import Library

[![DOI](https://zenodo.org/badge/78396315.svg)](https://zenodo.org/badge/latestdoi/78396315)

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

# Mathematica package

For Mathematica users, the library contains a package which interfaces the underlying Java library and exposes many features through static functions.
Most information are returned as `Assiciations` or as primitive tensors.
You can install and load the package in Mathematica 11 directly from the latest release on GitHub:

```mathematica
PacletInstall["https://github.com/halirutan/spectralis-raw-data/releases/download/v1.2/HSF-1.2.paclet"];

<<HSF`
```

The repository contains sample scans that can be used for testing.
For instance, to check the meta-data of a scan, access the image of the 20th B-scan,
and check the meta-data of this B-scan, you can use:

```mathematica
file = "/path/to/spectralis-raw-data/test-data/layers.vol";

HSFInfo[file]
HSFBScanImage[file, 20]
HSFBScanInfo[file, 20]
```

If loading information from a file results in an error message which indicates that the
link to Java died, then this is most likely happening because of insufficient memory available to the
Java virtual machine.
Please use, e.g. 

```mathematica
<< JLink`;
ReinstallJava[JVMArguments -> "-Xmx2048m"];
```

**before** loading the `HSF` package to increase the memory.

Here is an example which uses the `file-with-layers.vol` sample-file from the repository to plot the containing
retinal layers.

```mathematica
file = "/path/to/file-with-layers.vol";
invalidEntry = $HSFInvalid;
info = HSFInfo[file];
layers = HSFLayerSegmentation[file, 50];

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

To see which functions are provided by the `HSF` package, please see the usage
messages at the top of the [`HSF.m`](https://github.com/halirutan/spectralis-raw-data/blob/master/Spectralis/HSF/Kernel/HSF.m)
package.
If you run into issues or strange behavior, please [open a new issue](https://github.com/halirutan/spectralis-raw-data/issues/new).

# Citing this library in science

* Patrick Scheibe. *Spectralis Raw Data Import Library*, 2019, Version v1.2, http://doi.org/10.5281/zenodo.3236491
