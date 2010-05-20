class Texto extends FBox {
  
  String texto;
  
  Texto(String _texto){
    super(textWidth(_texto), textAscent() + textDescent()+4);
    texto = _texto;
  }
  
  void draw(PGraphics applet) {
    super.draw(applet);

    preDraw(applet);
    fill(0);
    stroke(0);
    textAlign(CENTER);
    text(texto, 0, textDescent());
    postDraw(applet);
  }
  
}
