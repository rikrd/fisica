/**
 *  Buttons and bodies
 *
 *  by Ricard Marxer
 *
 *  This example shows how to create a blob.
 */

import fisica.*;

FWorld world;

int circleCount = 20;
float hole = 50;
float topMargin = 50;
float bottomMargin = 300;
float sideMargin = 100;
float xPos = 0;

void setup() {
  size(400, 400);
  smooth();

  Fisica.init(this);

  world = new FWorld();
  world.setGravity(0, -300);

  FPoly l = new FPoly();
  l.vertex(width/2-hole/2, 0);
  l.vertex(0, 0);
  l.vertex(0, height);
  l.vertex(0+sideMargin, height);
  l.vertex(0+sideMargin, height-bottomMargin);
  l.vertex(width/2-hole/2, topMargin);
  l.setStatic(true);
  l.setFill(0);
  l.setFriction(1);
  world.add(l);

  FPoly r = new FPoly();
  r.vertex(width/2+hole/2, 0);
  r.vertex(width, 0);
  r.vertex(width, height);
  r.vertex(width-sideMargin, height);
  r.vertex(width-sideMargin, height-bottomMargin);
  r.vertex(width/2+hole/2, topMargin);
  r.setStatic(true);
  r.setFill(0);
  r.setFriction(1);
  world.add(r);
}

void draw() {
  background(80, 120, 200);

  if ((frameCount % 20) == 1) {
    FBlob b = new FBlob();
    float s = random(30, 40);
    float space = (width-sideMargin*2-s);
    xPos = (xPos + random(s, space/2)) % space;
    b.setAsCircle(sideMargin + xPos+s/2, height-random(100), s, 20);
    b.setStroke(0);
    b.setStrokeWeight(2);
    b.setFill(255);
    world.add(b);
  }

  world.step();
  world.draw();
}


void keyPressed() {
  try {
    saveFrame("screenshot.png");
  } 
  catch (Exception e) {
  }
}




