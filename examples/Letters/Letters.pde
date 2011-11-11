/**
 *  Falling letters <br/><br/>
 *
 *  by Ricard Marxer <br/><br/>
 *
 *  This example shows how the use <a href="http://www.ricardmarxer.com/geomerative">Geomerative</a> to access
 *  the contours of letters and create them as physical objects.<br/><br/>
 *
 *  <b>Click on the applet and type!</b>
 */

import fisica.*;
import geomerative.*;
import org.apache.batik.svggen.font.table.*;
import org.apache.batik.svggen.font.*;

FWorld world;
float posX = 0;
RFont font;

void setup() {
  size(400, 400);
  smooth();

  frameRate(60);

  Fisica.init(this);
  Fisica.setScale(4);
  RG.init(this);

  RG.setPolygonizer(RG.ADAPTATIVE);

  world = new FWorld();
  world.setGravity(0, 200);
  world.setEdges(this, color(255));
  world.remove(world.top);

  font = RG.loadFont("LiberationSerif-Bold.ttf");
}

void draw() {
  background(255);
  fill(0);
  stroke(0);
  world.draw(this);
  world.step();
}

void keyPressed() {  
  FChar chr = new FChar(key);
  if (chr.bodyCreated()) {
    world.add(chr);
  }

  if (key == ' ') {
    world.clear();
    world.setEdges(this, color(255));
    world.remove(world.top);
    posX = 0;
  }

  try {
    if (keyCode==CONTROL) {
      saveFrame("screenshot.png");
    }
  } 
  catch (Exception e) {
  }
}

