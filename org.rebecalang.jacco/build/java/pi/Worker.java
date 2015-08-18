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
 * The Worker class for a simple example using a split/merge scheme to calculate
 * the value of pi. Translated from a MPI example found at:
 * http://www-unix.mcs.anl.gov/mpi/usingmpi/examples/simplempi/main.htm
 * 
 * @author Steven Lauterburg (steven.lauterburg@gmail.com)
 * 
 */
public class Worker extends Actor {
	
	@Exclude
	private static final long serialVersionUID = 1788369284597025455L;
  
	private final int id;
	private final int nbWorkers;

  public Worker(Integer id, Integer nb) {
    this.id = id;
    this.nbWorkers = nb;
  }

  @message
  public void intervals(ActorName sender, int n) {
    double h = 1.0 / n;

    double sum = 0;
    for (int i = id; i <= n; i += nbWorkers) {
      double x = h * (i - 0.5);
      sum += (4.0 / (1.0 + x * x));
    }

    send(sender, "sum", h * sum);
  }

  @message
  public void stop() {
//    destroy("stop");
  }

}
