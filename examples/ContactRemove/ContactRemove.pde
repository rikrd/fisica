/**
 *  ContactRemove
 *
 *  by Ricard Marxer
 *
 *  This example shows how to use the contact events in order to remove bodies.
 */

import fisica.*;

FWorld world;
FBox pala;

void setup() {
  size(400, 400);
  smooth();

  Fisica.init(this);

  world = new FWorld();

  pala = new FBox(50, 20);
  pala.setPosition(width/2, height - 40);
  pala.setStatic(true);
  pala.setFill(0);
  pala.setRestitution(0);
  world.add(pala);
}

void draw() {
  background(255);

  if (frameCount % 8 == 0) {
    FCircle b = new FCircle(random(5, 20));
    b.setPosition(random(0+10, width-10), 50);
    b.setVelocity(0, 200);
    b.setRestitution(0);
    b.setNoStroke();
    b.setFill(200, 30, 90);
    world.add(b);
  }
  
  pala.setPosition(mouseX, height - 40);
  
  world.draw();
  world.step();
}

void contactStarted(FContact c) {
  FBody ball = null;
  if (c.getBody1() == pala) {
    ball = c.getBody2();
  } else if (c.getBody2() == pala) {
    ball = c.getBody1();
  }
  
  if (ball == null) {
    return;
  }
  
  ball.setFill(30, 190, 200);
  world.remove(ball);
}

void keyPressed() {
  try {
    saveFrame("screenshot.png");
  } 
  catch (Exception e) {
  }
}

