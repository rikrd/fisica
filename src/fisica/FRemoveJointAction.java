package fisica;

class FRemoveJointAction extends FWorldAction {
  protected FJoint m_joint;
  
  FRemoveJointAction(FJoint joint) {
    m_joint = joint;
  }
  
  protected void apply(FWorld world) {
    world.removeJoint(m_joint);
  }
}