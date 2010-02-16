import fisica.*;

FWorld mundo;
FBox caja;

void setup() {
  size(400, 400);
  smooth();
  
  Fisica.init(this);
  
  mundo = new FWorld();
  mundo.setGravity(0, 200);
  
  frameRate(24);
}

void draw() {
  background(0);
  
  if ((frameCount % 24) == 0) {
    FCircle bolita = new FCircle(10);
    bolita.setNoStroke();
    bolita.setFillColor(255);
    bolita.setPosition(100, 20);
    bolita.setVelocity(0, 300);
    bolita.setRestitution(0.9);
    bolita.setDamping(0);
    mundo.add(bolita);
  }
  
  mundo.step();
  mundo.draw(this);
}

void mousePressed() {
  caja = new FBox(200, 10);
  caja.setFillColor(255);
  caja.setPosition(mouseX, mouseY);
  caja.setStaticBody(true);
  caja.setRestitution(0.9);
  mundo.add(caja);
}

void mouseDragged() {
  if (caja == null) {
    return;
  }
  
  float ang = atan2(caja.getY() - mouseY, caja.getX() - mouseX);
  caja.setRotation(ang);
}

void contactStarted(FContact contacto) {
  FBody cuerpo1 = contacto.getBody1();
  cuerpo1.setFillColor(255, 0, 0);
  
  noFill();
  stroke(255);
  ellipse(contacto.getX(), contacto.getY(), 30, 30);
}

void contactEnded(FContact contacto) {
  FBody cuerpo1 = contacto.getBody1();
  cuerpo1.setFillColor(255);
}
