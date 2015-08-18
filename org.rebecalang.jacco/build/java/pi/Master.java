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

import org.rebecalang.javactor.actor.Exclude;

import osl.manager.Actor;
import osl.manager.ActorName;
import osl.manager.annotations.message;

/**
 * The Master class for a simple example using a split/merge scheme to calculate
 * the value of pi. Translated from a MPI example found at:
 * http://www-unix.mcs.anl.gov/mpi/usingmpi/examples/simplempi/main.htm
 * 
 * @author Steven Lauterburg (steven.lauterburg@gmail.com)
 * 
 */
public class Master extends Actor {

	@Exclude
	private static final long serialVersionUID = -5849269763602140291L;

  private final ActorName[] workers;

  private int counter = 0;
  private double result = 0.0;

  public Master(ActorName[] workers) {
    this.workers =  workers;
  }

  @message
  public void boot() {
    int n = 1000;

    for (ActorName w : workers) {
      send(w, "intervals", self(), n);
    }
  }

  @message
  public void sum(double p) {
    counter++;
    result += p;
    if (counter == workers.length) {
      for (ActorName w : workers) {
        send(w, "stop");
      }
//      destroy("stop");
    }
  }

}
