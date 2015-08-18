/**
 * Copyright (c) 1998-2009  The University of Illinois Board of Trustees.
 * All Rights Reserved.
 * 
 * Distributed under license: http://osl.cs.uiuc.edu/af/LICENSE
 * 
 * Developed by: The Open Systems Lab
 *               University of Illinois at Urbana-Champaign
 *               Department of Computer Science
 *               Urbana, IL 61801
 *               http://osl.cs.uiuc.edu
 *
 * Contact: http://osl.cs.uiuc.edu/af
 *
 */
package shortestpath;

import java.util.Vector;
import java.util.Iterator;
import osl.manager.annotations.message;
import osl.manager.ActorName;
import osl.manager.RemoteCodeException;

public class SPActor extends osl.manager.Actor {

  private int D = -1;
  private int id = -1;
  private Vector<ActorName> neigbors = new Vector<ActorName>();
  private Vector<Integer> neigborsD = new Vector<Integer>();

  public SPActor(Integer id) {
    this.id = id;
  }

  @message
  public void addNeighbor(osl.manager.ActorName n, Integer d) {
    neigbors.add(n);
    neigborsD.add(d);
  }

  @message
  public void receive(Integer d, Integer w) throws RemoteCodeException {
    // System.out.println("receiving " + d + " " + w + " at " + id);
    if (D == -1 || D > d + w) {
      D = d + w;
      Iterator<Integer> iter2 = neigborsD.iterator();
      for (Iterator<ActorName> iterator = neigbors.iterator(); iterator
          .hasNext();) {
        osl.manager.ActorName spProcess = iterator.next();
        Integer w1 = iter2.next();
        // System.out.println("   - " + id + " Sending to " + spProcess);
        send(spProcess, "receive", D, w1);
      }
    } /* else */
    // System.out.println(id + ": Current distance to X = " + D);
  }

}