import fisica.*;

FBox cub;
FWorld world;

void setup(){
  size(800, 600);
  smooth();
  
  world = new FWorld(this);
  cub = new FBox(world, 20, 20);
  cub.setDensity(1.0);
  cub.setPosition(100, 100);
  cub.setVelocity(0, 10);
}

void draw(){
  world.step();
  world.draw(this);
}
