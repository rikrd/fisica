import fisica.*;

String msg = "";
FWorld world;
PFont font;

void setup(){
  size(400, 400);
  smooth();

  Fisica.init(this);

  font = loadFont("AndaleMono-18.vlw");
  textFont(font, 18);

  world = new FWorld();
  world.setEdges(this, color(80, 120, 160));
  world.remove(world.left);
  world.remove(world.right);
  world.remove(world.top);
  world.setGravity(0, 500);

  Texto t = new Texto("Type and ENTER");
  t.setPosition(width/2, height/2);
  t.setRotation(random(-1, 1));
  t.setFill(255);
  t.setNoStroke();
  t.setRestitution(0.75);
  world.add(t);
}

void draw() {
  background(80, 120, 160);

  world.step();
  world.draw(this);
}

void keyPressed() {
  if (key == ENTER) {
    if (!msg.equals("")) {
      Texto t = new Texto(msg);
      t.setPosition(width/2, height/2);
      t.setRotation(random(-1, 1));
      t.setFill(255);
      t.setNoStroke();
      t.setRestitution(0.75);
      world.add(t);
      msg = "";
    }
  } 
  else {
    msg+= key;
  }
}

