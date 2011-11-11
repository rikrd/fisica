import fisica.*;

String msg = "";
FWorld world;
PFont font;

void setup(){
  size(400, 400);
  smooth();

  Fisica.init(this);

  font = loadFont("FreeMonoBold-24.vlw");
  textFont(font, 24);

  world = new FWorld();
  world.setEdges(this, color(120));
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
  background(120);

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
      t.setRestitution(0.65);
      world.add(t);
      msg = "";
    }
  }  
  else if (key == CODED && keyCode == CONTROL) {
    try {
      saveFrame("screenshot.png");
    } 
    catch (Exception e) {
    }
  }
  else {
    msg+= key;
  }
}

