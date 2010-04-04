/**
 *  Polygons
 *
 *  by Ricard Marxer
 *
 *  This example shows how to create polygon bodies.
 */

import fisica.*;

FWorld world;
FPoly poly;

void setup() {
  size(400, 400);
  smooth();

  Fisica.init(this);

  world = new FWorld();
  world.setGravity(0, 800);
  world.setEdges();
  world.remove(world.left);
  world.remove(world.right);
  world.remove(world.top);
  
  world.setEdgesRestitution(0.5);
}

void draw() {
  background(255);

  world.step();
  world.draw(this);  

  // Draw the polygon while
  // while it is being created
  // and hasn't been added to the
  // world yet
  if (poly != null) {
    poly.draw(this);
  }
}


void mousePressed() {
  if (world.getBody(mouseX, mouseY) != null) {
    return;
  }

  poly = new FPoly();
  poly.setStrokeWeight(3);
  poly.setFill(120, 30, 90);
  poly.setDensity(10);
  poly.setRestitution(0.5);
  poly.vertex(mouseX, mouseY);
}

void mouseDragged() {
  if (poly!=null) {
    poly.vertex(mouseX, mouseY);
  }
}

void mouseReleased() {
  if (poly!=null) {
    world.add(poly);
    poly = null;
  }
}

void keyPressed() {
  if (key == BACKSPACE) {
    FBody hovered = world.getBody(mouseX, mouseY);
    if ( hovered != null &&
         hovered.isStatic() == false ) {
      world.remove(hovered);
    }
  } 
  else {
    try {
      saveFrame("screenshot.png");
    } 
    catch (Exception e) {
    }
  }
}




