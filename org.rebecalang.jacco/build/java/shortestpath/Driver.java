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
package shortestpath;

import org.rebecalang.javactor.actor.ReactiveSystem;

import osl.manager.Actor;
import osl.manager.ActorName;
import osl.manager.RemoteCodeException;
import osl.manager.annotations.message;

/**
 * This class is a Basset test driver for a actor-based implementation of a
 * shortest path algorithm.
 * 
 * This driver includes three different graphs of size 3, 4 and 5 nodes
 * respectively.
 * 
 * @author Steven Lauterburg (steven.lauterburg@gmail.com)
 * 
 */
public class Driver extends Actor {

  int N = 5; // can specify 3, 4 or 5
  ActorName a0, a1, a2, a3, a4;

  @message
  public void setUp() throws RemoteCodeException {
//    if (args.length >= 1) {
//      N = Integer.parseInt(args[0]);
//    }
//    if (N < 3 || N > 5) {
//      throw new RemoteCodeException("Invalid graph number. Must be 3, 4 or 5");
//    }
    System.out.println("shortestpath graph number = " + N);

    // create root set actors
    a0 = create(SPActor.class, 1);
    a1 = create(SPActor.class, 2);
    a2 = create(SPActor.class, 3);
    a3 = create(SPActor.class, 4);
    a4 = create(SPActor.class, 5);
    
    SPActor actor0 = (SPActor)ReactiveSystem.getInstance().getActor(a0.getId());
    SPActor actor1 = (SPActor)ReactiveSystem.getInstance().getActor(a1.getId());
    SPActor actor2 = (SPActor)ReactiveSystem.getInstance().getActor(a2.getId());
    SPActor actor3 = (SPActor)ReactiveSystem.getInstance().getActor(a3.getId());
    SPActor actor4 = (SPActor)ReactiveSystem.getInstance().getActor(a4.getId());
    // initialize actors
    if (N == 3) {

    	actor0.addNeighbor(a1, 10);
    	actor0.addNeighbor(a2, 10);
    	actor1.addNeighbor(a0, 10);
    	actor1.addNeighbor(a2, 10);
    	actor2.addNeighbor(a0, 10);
    	actor2.addNeighbor(a1, 10);
    	
//      call(a0, "addNeighbor", a1, 10);
//      call(a1, "addNeighbor", a0, 10);
//      call(a0, "addNeighbor", a2, 10);
//      call(a2, "addNeighbor", a0, 10);
//      call(a1, "addNeighbor", a2, 10);
//      call(a2, "addNeighbor", a1, 10);

    } else if (N == 4) {
    	
    	actor0.addNeighbor(a1, 10);
    	actor0.addNeighbor(a2, 10);
    	actor0.addNeighbor(a3, 10);
    	actor1.addNeighbor(a3, 10);
    	actor2.addNeighbor(a3, 10);
    	actor3.addNeighbor(a1, 10);
    	actor3.addNeighbor(a2, 10);

//      call(a0, "addNeighbor", a1, 10);
//      // call(a1, "addNeighbor", a0, 10);
//      call(a0, "addNeighbor", a2, 10);
//      // call(a2, "addNeighbor", a0, 10);
//      call(a0, "addNeighbor", a3, 10);
//      // call(a3, "addNeighbor", a0, 10);
//      call(a1, "addNeighbor", a3, 10);
//      call(a3, "addNeighbor", a1, 10);
//      call(a2, "addNeighbor", a3, 10);
//      call(a3, "addNeighbor", a2, 10);

    } else { // N == 5
    	
    	actor0.addNeighbor(a1, 10);
    	actor0.addNeighbor(a2, 10);
    	actor1.addNeighbor(a0, 10);
    	actor1.addNeighbor(a3, 10);
    	actor2.addNeighbor(a0, 10);
    	actor3.addNeighbor(a1, 10);
    	actor4.addNeighbor(a0, 10);
    	actor4.addNeighbor(a1, 10);
    	actor1.addNeighbor(a4, 10);
    	actor0.addNeighbor(a4, 10);
    	

//      call(a0, "addNeighbor", a1, 10);
//      call(a1, "addNeighbor", a0, 10);
//      call(a0, "addNeighbor", a2, 10);
//      call(a2, "addNeighbor", a0, 10);
//      call(a1, "addNeighbor", a3, 10);
//      call(a3, "addNeighbor", a1, 10);
//      // call(a2, "addNeighbor", a3 ,10 ) ;
//      // call(a3, "addNeighbor", a2 ,10 ) ;
//      call(a4, "addNeighbor", a0, 10);
//      call(a4, "addNeighbor", a1, 10);
//      call(a1, "addNeighbor", a4, 10);
//      call(a0, "addNeighbor", a4, 10);

    }
    
    send(self(), "test");
  }

  @message
  public void test() {
    // queue initial messages
    if (N == 3) {
      send(a0, "receive", 0, 0);
    } else if (N == 4) {
      send(a0, "receive", 0, 0);
    } else { // N == 5
      send(a4, "receive", 0, 0);
    }
  }

}
