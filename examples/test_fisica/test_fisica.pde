import fisica.*;

FBox cub, cub2;
FCircle circ;
FWorld world;

void setup(){
  size(400, 400, P2D);
  smooth();
  
  world = new FWorld(this);
  world.setGravity(0, 100);
  
  cub = new FBox(world, 20, 20);
  cub.setPosition(100, 100);
  cub.setVelocity(0, 0);
  cub.setDensity(1.0);

  circ = new FCircle(world, 20);
  circ.setPosition(100, 300);
  circ.setVelocity(0, -200);
  circ.setDensity(1.0);

  cub2 = new FBox(world, 20, 20);
  cub2.setPosition(100, 140);
  cub2.setRotation(radians(45));
  cub2.setVelocity(0, 0);
  cub2.setAngularVelocity(radians(150));
  cub2.setDensity(1.0);
}

void draw(){
  background(0);
  world.step();
  world.draw(this);
}
