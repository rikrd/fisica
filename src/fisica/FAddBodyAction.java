package fisica;

class FAddBodyAction extends FWorldAction {
  protected FBody m_body;
  
  FAddBodyAction(FBody body) {
    m_body = body;
  }
  
  protected void apply(FWorld world) {
    world.addBody(m_body);
  }
}