import fisica.*;

FWorld world;

void setup(){
  size(400, 400, P2D);
  smooth();

  world = new FWorld(this);
  world.setGravity(0, 100);

  FBody cub = new FBox(world, 20, 20);
  cub.setPosition(100, 100);
  cub.setVelocity(0, 0);
  cub.setDensity(1.0);

  FBody circ = new FCircle(world, 20);
  circ.setPosition(100, 300);
  circ.setVelocity(0, -200);
  circ.setDensity(1.0);

  FBody cub2 = new FBox(world, 20, 20);
  cub2.setPosition(100, 140);
  cub2.setRotation(radians(45));
  cub2.setVelocity(0, 0);
  cub2.setAngularVelocity(radians(150));
  cub2.setDensity(1.0);
}

void draw(){
  background(0);

  if ( mousePressed ) {
    FBody cub2 = new FBox(world, 20, 20);
    cub2.setPosition(mouseX, mouseY);
    cub2.setRotation(radians(random(360)));
    cub2.setVelocity(random(-1000, 1000), random(-1000, 1000));
    cub2.setAngularVelocity(radians(random(3600)));
    cub2.setDensity(1.0);    
  }

  world.step();
  world.draw(this);
}

