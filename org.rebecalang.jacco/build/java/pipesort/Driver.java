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

import osl.manager.Actor;
import osl.manager.ActorName;
import osl.manager.RemoteCodeException;
import osl.manager.annotations.message;

/**
 * This class is a Basset test driver for a simple actor-based sorting example.
 * 
 * @author Rajesh K. Karmani (rkumar8@illinois.edu)
 * 
 */
public class Driver extends Actor {

  int N = 5;  // can specify 1 or higher
  ActorName left;

  @message
  public void setUp() throws RemoteCodeException {
//    if (args.length >= 1) {
//      N = Integer.parseInt(args[0]);
//    }
    System.out.println("pipesort bound (# of nodes) = " + N);

    // create root set actors
    left = create(LeftActor.class);
    send(self(), "test");
  }

  @message
  public void test() {
    // queue initial messages
    send(left, "feed", N);
  }

}
