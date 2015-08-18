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
package pi;

import osl.manager.Actor;
import osl.manager.ActorName;
import osl.manager.RemoteCodeException;
import osl.manager.annotations.message;

/**
 * This class is a Basset test driver for a simple actor-based example using a
 * split/merge scheme to calculate the value of pi.
 * 
 * @author Steven Lauterburg (steven.lauterburg@gmail.com)
 * 
 */
public class Driver extends Actor {

  int N = 6; // can specify 1 or higher

  ActorName[] workers;
  ActorName master;

  @message
  public void setUp() throws RemoteCodeException {

    // create root set actors
//    if (args.length >= 1) {
//      N = Integer.parseInt(args[0]);
//    }
    System.out.println("pi bound (# of workers) = " + N);

    // create root set actors
    workers = new ActorName[N];
    for (int i = 0; i < N; i++)
      workers[i] = create(Worker.class, i, N);

    master = create(Master.class, (java.io.Serializable) workers);
    if (master == null){
    	System.out.println("null master");
    }
    send(self(), "test");
  }

  @message
  public void test() {

    // queue initial messages
    send(master, "boot");
  }

}
