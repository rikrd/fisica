package fisica;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.*;

class FContactID {
  protected ContactID m_id;

  public FContactID(ContactID id) {
    m_id = id;
  }

  public FContactID(FContactID fid) {
    m_id = new ContactID(fid.m_id);
  }

  public int hashCode()
  {
    return m_id.features.flip
      + m_id.features.incidentVertex*2
      + m_id.features.referenceEdge*2*255
      + m_id.features.incidentEdge*2*255*255;
  }

  public boolean equals(Object obj)
  {
    if(this == obj)
      return true;

    if((obj == null) || (obj.getClass() != this.getClass()))
      return false;

    // object must be Test at this point
    FContactID test = (FContactID)obj;
    return m_id.isEqual(test.m_id);
  }

}
