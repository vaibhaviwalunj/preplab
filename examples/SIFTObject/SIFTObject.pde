/**
*  SIFT _ An example applet on how to use Scale Invariant Feature Transform <br><br>
*
*  All the artist needs to do is to call Sifter.runSift(PImage) on any PImage they have<br>
*  loaded from a file or read() from a camera stream. <br>
*  The below part shows how to iterate through these features and one possible way of visualizing them
*
*  Barkin Aygun - 1/6/2009<br><br>
*  This applet is entirely for my work as the Java Programmer<br>
*  to be used by everyone in the Art That Learns class, and will<br>
*  not be used in anyway other than a reference for class work by me.<br>
*
*/

import imagelib.*;

void setup() {
stroke(0,255,255);
noFill();

// Load and show the image
PImage img = loadImage("box.jpg");
size(img.width, img.height);
image(img,0,0);

// All you need to do, to run sift on an image
Vector<Feature> fs = Sifter.runSift(img);

// System output to see results of SIFT
println("The number of features: " + fs.size());
// An example code on how to iterate through features
Iterator<Feature> fsiter = fs.iterator();
 
while (fsiter.hasNext()) {
  Feature f = fsiter.next();
  pushMatrix();
  translate(f.location[0], f.location[1]);
  rotate(f.orientation);
  float fsize = 4 * f.scale;
  ellipse(0,0, fsize, f.scale);
  popMatrix();
 
}
}
