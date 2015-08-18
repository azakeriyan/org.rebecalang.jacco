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
package quicksort;

import mergesort.MsgMergesorter;
import osl.manager.Actor;
import osl.manager.ActorName;
import osl.manager.RemoteCodeException;
import osl.manager.annotations.message;
//import gov.nasa.jpf.actor.core.PlatformUtil;

/**
 * This class is a Basset test driver for a actor-based quick sort
 * implementation.
 * 
 * @author Bobak Hadidi (bhadidi2@illinois.edu)
 * 
 */
public class Driver extends Actor {

  int N = 5;
  ActorName sorter;

  @message
  public void setUp() throws RemoteCodeException {
//    if (args.length >= 1) {
//      N = Integer.parseInt(args[0]);
//    }
    System.out.println("quicksort bound = " + N);

    // create root set actors
    sorter = create(MsgQuicksorter.class);
    send(self(), "test");
  }

  @message
  public void test() {
    // queue initial messages
    send(sorter, "boot", N);
  }

}
