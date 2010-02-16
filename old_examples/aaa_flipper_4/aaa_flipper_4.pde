import fisica.*;

FWorld mundo;
FCircle bola;
FPoly flipperIzq;
FRevoluteJoint junta;

void setup(){
  size (200, 400);
  smooth();

  //creamos la bola en setup para que tan solo aparezca una vez
  Fisica.init(this);
  mundo = new FWorld();
  mundo.setGravity(0, 300);
  
  //creamos la bola
  bola = new FCircle(10);
  bola.setNoStroke();
  bola.setPosition(40, 20);
  bola.setBullet(true);
  mundo.add(bola);
  
  // Ponemos bordes
  mundo.setEdges(this, color(255));
  
  // Quitamos el borde de abajo
  mundo.remove(mundo.bottom); 
  
  float flipperWidth = 50;
  flipperIzq = new FPoly();
  flipperIzq.vertex(-flipperWidth/5, 0);
  flipperIzq.vertex(0, flipperWidth/5);
  flipperIzq.vertex(flipperWidth*9/10, flipperWidth/10);
  flipperIzq.vertex(flipperWidth, 0);
  flipperIzq.vertex(flipperWidth*9/10, -flipperWidth/10);
  flipperIzq.vertex(0, -flipperWidth/5);
  flipperIzq.setPosition(20, height-20);
  flipperIzq.setFillColor(255);
  flipperIzq.setBullet(true);
  mundo.add(flipperIzq);

  junta = new FRevoluteJoint(flipperIzq, mundo.left, 20, height-20);
  junta.setEnableLimit(true);
  junta.setLowerAngle(0.0);
  junta.setUpperAngle(PI/3);
  junta.setDrawable(false);
  mundo.add(junta);
}


void draw(){
  background(0);
  
  mundo.step();
  
  // siempre debemos reposicionar el flipper
  // para que no se "caiga"
  // no podemos hacer que sea estatico porque
  // entonces la velocidad angular no haría nada
  //flipperIzq.setPosition(20, height-20);
  
  mundo.draw(this);
}

void keyPressed() {
  if(key=='z') {
     flipperIzq.setAngularVelocity(-50);
  }
}

void keyReleased() {
  if(key=='z') {
     flipperIzq.setAngularVelocity(50);
  }
}
