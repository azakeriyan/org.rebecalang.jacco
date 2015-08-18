//
// Copyright (C) 2010 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
//
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
//
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//
package pipesort;

import org.rebecalang.javactor.actor.Exclude;

import osl.manager.Actor;
import osl.manager.ActorName;
import osl.manager.RemoteCodeException;
import osl.manager.annotations.message;

/**
 * This class implements the left-most (i.e., first) stage of a simple
 * actor-based pipeline used for sorting.
 * 
 * @author Rajesh K. Karmani (rkumar8@illinois.edu)
 * 
 */
public class LeftActor extends Actor {

	private ActorName out;
	
	private java.util.Random rand = new java.util.Random(9999);

  @message
  public void feed(Integer total) throws RemoteCodeException {
    if (total == 0) {
      return;
    }
    if (out == null)
      out = create(MidActor.class);
    send(out, "push", rand.nextInt(1000));
    send(self(), "feed", total - 1);
  }

}