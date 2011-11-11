/**
 *  Densities
 *
 *  by Ricard Marxer
 *
 *  This example shows how the density works.
 *  The density determines the mass per area of a body.
 *  In this example we show a column of balls all of same area and increasing densities from 0.1 to 0.9.
 *  These balls will collide against another column of balls all with the same density of 0.9.
 *  We can observe the different behavior of the collisions depending on the density.
 *
 *  Note that a density of 0.0 corresponds to a mass o 0 and the body will be considered static.
 */

import fisica.*;

FWorld world;
int ballCount = 9;

void setup() {
  size(400, 400);
  smooth();

  Fisica.init(this);

  world = new FWorld();
  world.setGravity(0, 0);
  world.setEdges();
  world.remove(world.left);
  world.remove(world.top);
  world.remove(world.bottom);

  for (int i=0; i<ballCount; i++) {
    FCircle b = new FCircle(25);
    b.setPosition(40, map(i, 0, ballCount-1, 40, height-40));
    b.setDensity(map(i, 0, ballCount-1, 0.1, 0.9));
    b.setVelocity(100, 0);
    b.setDamping(0.0);
    b.setNoStroke();
    b.setFill(map(i, 0, ballCount-1, 120, 0));
    world.add(b);
  }

  for (int i=0; i<ballCount; i++) {
    FCircle b = new FCircle(25);
    b.setPosition(width/2, map(i, 0, ballCount-1, 40, height-40));
    b.setVelocity(0, 0);
    b.setDamping(0.0);
    b.setDensity(0.9);
    b.setNoStroke();
    b.setFill(125, 80, 120);
    world.add(b);
  }

}

void draw() {
  background(255);

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

