/**
 *  Winged Star</br></br>
 *
 *  by Ricard Marxer</br></br>
 *
 *  It shows how to create bodies from SVG files.
 *  The used SVG file must contain two elements.
 *  One element with the id 'object'
 *  which will be used to draw the body.
 *  Another element must have the id 'outline'
 *  and it will be used as the contour
 *  of the body.</br></br>
 *
 *  This example require the library <a href="http://www.ricardmarxer.com/geomerative">Geomerative</a>.
 */

import fisica.*;
import geomerative.*;

FWorld world;
String filename = "winged_star.svg";
  
void setup(){
  size(400, 400);
  smooth();
  
  frameRate(60);

  Fisica.init(this);
  Fisica.setScale(10);
  
  RG.init(this);
  
  RG.setPolygonizer(RG.ADAPTATIVE);

  world = new FWorld();
  world.setEdges(this, color(0));
  world.setGravity(0, 300);
    
  createWingedStar(width/2, height/2);
}

void draw(){
  background(255);

  world.draw(this);
  world.step();
}

void mousePressed(){
  if (world.getBody(mouseX, mouseY) == null) {
    createWingedStar(mouseX, mouseY);
  }
}

void keyPressed() {
  try {
    saveFrame("screenshot.png");
  } 
  catch (Exception e) {
  }
}

void createWingedStar(float x, float y) {
  float angle = random(TWO_PI);
  float magnitude = 200;
  
  FSVG obj = new FSVG(filename);
  obj.setPosition(x, y);
  obj.setRotation(angle+PI/2);
  obj.setVelocity(magnitude*cos(angle), magnitude*sin(angle));
  obj.setDamping(0);
  obj.setRestitution(0.5);
  world.add(obj);
}
