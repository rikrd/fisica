/**
 *  Densities
 *
 *  by Ricard Marxer
 *
 *  This example shows how the density coefficients works.
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
