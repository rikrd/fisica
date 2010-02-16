class Texto extends FBox {
  
  String texto;
  
  Texto(String _texto){
    super(textWidth(_texto), textAscent() + textDescent());
    texto = _texto;
  }
  
  void draw(PApplet applet) {
    preDraw(applet);
    fill(0);
    stroke(0);
    textAlign(CENTER);
    text(texto, 0, textDescent()+10);
    postDraw(applet);
  }
  
}
