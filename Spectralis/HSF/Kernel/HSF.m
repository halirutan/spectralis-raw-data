(* Mathematica Package *)
(* Created by Mathematica Plugin for IntelliJ IDEA, see http://wlplugin.halirutan.de/ *)

(* :Title: Spectralis *)
(* :Context: Spectralis` *)
(* :Author: patrick *)
(* :Date: 2018-04-27 *)

(* :Mathematica Version: 11 *)
(* :Copyright: (c) 2018 patrick *)
(* :Keywords: *)
(* :Discussion: *)

BeginPackage["HSF`"];

$HSFInvalid::usage = "$HSFInvalid is a numerical value used to indicate that e.g. a segmentation-value is invalid.";
HSFFileQ::usage = "HSFFileQ[file] returns true if the file exists and has the extension \"vol\"";
HSFInfo::usage = "HSFInfo[file] reads the file header information from the OCT scan.";
HSFBScanInfo::usage = "HSFBScanInfo[file] reads the BScan header information of all BScans." <>
    "HSFBScanInfo[file, index] reads the BScan header information at index.";
HSFSloImage::usage = "HSFSloImage[file] reads the SLO image from the OCT scan.";
HSFBScanData::usage = "HSFBScanData[file] reads the header information of all BScans.\n" <>
    "HSFBScanData[file, index] reads the header information of BScan at index.";
HSFBScanImage::usage = "HSFBScanImage[file, index] creates an image of the B-scan at index.";
HSFGrid::usage = "HSFGrid[file, id] reads the measurement grid if available in file. Possible settings for id are 1 or 2.";
HSFLayerSegmentation::usage = "HSFLayerSegmentation[file] reads all segmented retinal layers from the OCT scan." <>
    "HSFLayerSegmentation[file, index] reads the layer segmentation at index.";

Begin["`Private`"];

$clazz = "de.halirutan.spectralis.mathematica.MmaHSF";
Needs["JLink`"];
JLink`LoadJavaClass[$clazz];

HSFFileQ[file_String] := TrueQ[FileExistsQ[file] && Not[DirectoryQ[file]] && FileExtension[file] === "vol"];
HSFFileQ[___] := False;

$HSFInvalid = MmaHSF`getInvalidFloatValue[];

HSFInfo[file_?HSFFileQ] := MmaHSF`getInfo[file];

HSFSloImage[file_?HSFFileQ] := MmaHSF`getSLOImage[file];

HSFBScanInfo[file_?HSFFileQ, index_ : All] := Switch[index,
  All,
  MmaHSF`getBScanInfo[file],
  _Integer,
  MmaHSF`getBScanInfo[file, index],
  _,
  $Failed
];

HSFLayerSegmentation[file_?HSFFileQ, index_ : All] := Switch[index,
  All,
  MmaHSF`getLayerSegmentation[file],
  _Integer,
  MmaHSF`getLayerSegmentation[file, index],
  _,
  $Failed
];

HSFGrid[file_?HSFFileQ, index : (1 | 2)] := MmaHSF`getGrid[file, index];

HSFBScanData[file_?HSFFileQ, index_ : All] := Switch[index,
  All,
  MmaHSF`getBScanData[file],
  _Integer,
  MmaHSF`getBScanData[file, index],
  _,
  $Failed
];

HSFBScanImage[file_?HSFFileQ, index_Integer] := Image[MmaHSF`getBScanData[file, index]^0.25]  /; 1 <= index <=  MmaHSF`getInfo[file]["NumBScans"];

End[]; (* `Private` *)

EndPackage[]
