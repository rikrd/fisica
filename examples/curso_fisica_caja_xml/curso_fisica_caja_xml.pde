import fisica.*;

FWorld world;
XMLElement xml;

void setup() {
  size(400, 400);
  smooth();

  // Cargamos el XML
  xml = new XMLElement(this, "mundo.xml");

  Fisica.init(this);

  world = new FWorld();
  world.setGravity(0, 400);
  world.setEdges(this, color(0));

  int numObjetos = xml.getChildCount();
  for (int i=0; i<numObjetos; i++) {
    XMLElement objeto = xml.getChild(i);

    if (objeto.getName().equals("caja")) {
      int x = objeto.getIntAttribute("x");
      int y = objeto.getIntAttribute("y");
      int w = objeto.getIntAttribute("width");
      int h = objeto.getIntAttribute("height");
      
      FBox caja = new FBox(w, h);
      caja.setPosition(x, y);
      world.add(caja);
    } 
    else if(objeto.getName().equals("bola")) {
      int x = objeto.getIntAttribute("x");
      int y = objeto.getIntAttribute("y");
      int r = objeto.getIntAttribute("radio");
      
      FCircle bola = new FCircle(r);
      bola.setPosition(x, y);
      world.add(bola);
    }
  }
}

void draw() {
  background(255);

  world.step();
  world.draw(this);
}

