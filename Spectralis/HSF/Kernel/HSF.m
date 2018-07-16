(* Mathematica Package *)
(* Created by Mathematica Plugin for IntelliJ IDEA, see http://wlplugin.halirutan.de/ *)

(* :Title: Spectralis *)
(* :Context: Spectralis` *)
(* :Author: patrick *)
(* :Date: 2018-04-27 *)

(* :Package Version: 0.1 *)
(* :Mathematica Version: 11 *)
(* :Copyright: (c) 2018 patrick *)
(* :Keywords: *)
(* :Discussion: *)

BeginPackage["HSF`"];

$HSFInvalid::usage = "$HSFInvalid is a numerical value used to indicate that e.g. a segmentation-value is invalid.";


Begin["`Private`"];

$clazz = "de.halirutan.spectralis.mathematica.MmaHSF";
Needs["JLink`"];
JLink`LoadJavaClass[$clazz];

volFileQ[file_String] := FileExistsQ[file] && Not[DirectoryQ[file]];
volFileQ[___] := False;

$HSFInvalid = MmaHSF`getInvalidFloatValue[];

HSFInfo[file_?volFileQ] := MathematicaInterface`getInfo[file];

HSFSloImage[file_?volFileQ] := MathematicaInterface`getSLOImage[file];

HSFBScanInfo[file_?volFileQ, index_ : All] := Switch[index,
  All,
  MathematicaInterface`getBScanInfo[file],
  _Integer,
  MathematicaInterface`getBScanInfo[file, index],
  _,
  $Failed
];

HSFLayerSegmentation[file_?volFileQ, index_ : All] := Switch[index,
  All,
  MathematicaInterface`getLayerSegmentation[file],
  _Integer,
  MathematicaInterface`getLayerSegmentation[file, index],
  _,
  $Failed
];

HSFGrid[file_?volFileQ, index : (1 | 2)] := MathematicaInterface`getGrid[file, index];

HSFBScanData[file_?volFileQ, index_ : All] := Switch[index,
  All,
  MathematicaInterface`getBScanData[file],
  _Integer,
  MathematicaInterface`getBScanData[file, index],
  _,
  $Failed
];

End[]; (* `Private` *)

EndPackage[]