/**
 *  Raycast
 *
 *  by Ricard Marxer
 *
 *  This example shows how to use the raycasts.
 */

import fisica.*;
import org.jbox2d.common.*;

FWorld world;
FBox obstacle;

void setup() {
  size(400, 400);
  smooth();

  Fisica.init(this);

  world = new FWorld();

  obstacle = new FBox(150,150);
  obstacle.setRotation(PI/4);
  obstacle.setPosition(width/2, height/2);
  obstacle.setStatic(true);
  obstacle.setFill(0);
  obstacle.setRestitution(0);
  world.add(obstacle);
}

void draw() {
  background(255);

  world.draw();
  world.step();
  
  castRay();
}

void castRay() {
  FRaycastResult result = new FRaycastResult();
  FBody b = world.raycastOne(width/2, height, mouseX, mouseY, result, true);

  stroke(0);
  line(width/2, height, mouseX, mouseY);

  if (b != null) {
    b.setFill(120, 90, 120);
    fill(180, 20, 60);
    noStroke();
    
    float x = result.getX();
    float y = result.getY();
    ellipse(x, y, 10, 10);

  } 
  else {
    obstacle.setFill(0);
  }
}

void keyPressed() {
  try {
    saveFrame("screenshot.png");
  } 
  catch (Exception e) {
  }
}

