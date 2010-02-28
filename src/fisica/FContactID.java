/*
  Part of the Fisica library - http://www.ricardmarxer.com/fisica

  Copyright (c) 2009 - 2010 Ricard Marxer

  Fisica is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.
  
  You should have received a copy of the GNU Lesser General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

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
