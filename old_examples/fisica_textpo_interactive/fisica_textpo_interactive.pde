import fisica.*;

FWorld world;
Texto t;
PFont font;

void setup(){
  size(400, 400);
  smooth();

  Fisica.init(this);

  font = loadFont("AndaleMono-48.vlw");
  textFont(font, 48);

  world = new FWorld();
  world.setEdges(this, color(255));
  world.setGravity(0, 500);
  
  t = new Texto("Blal");
  t.setPosition(width/2, height/2);
  world.add(t);
}

void draw() {
  background(255);
  
  world.step();
  world.draw(this);
}

void keyPressed() {
  if (key != CODED) {
    if (key == BACKSPACE) {
      if (t.getText().length() != 0) {
        t.setText(t.getText().substring(0, t.getText().length()-1));
      }
    } else {
      t.setText(t.getText() + key);
    }
  }
}
