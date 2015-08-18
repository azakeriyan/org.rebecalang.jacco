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
package mergesort;

import java.util.Random;

import org.rebecalang.javactor.actor.Exclude;

import osl.manager.*;
import osl.manager.annotations.*;

/**
 * This class is an actor-based implementation of a merge sort algorithm.
 * 
 * @author Bobak Hadidi (bhadidi2@illinois.edu)
 * 
 */
public class MsgMergesorter extends Actor {

	@Exclude
  private static final long serialVersionUID = 94805943859783L;

  private int received;
  private int[] half;
  private ActorName caller;

  public MsgMergesorter() {
    received = 0;
  }

  @message
  public void boot(Integer size) throws RemoteCodeException {
    if (size < 1) {
      System.out.println("Needs size>0");
      return;
    }

    Random rn = new Random(12);
    int[] arr = new int[size];
    for (int i = 0; i < size; i++)
      arr[i] = rn.nextInt(size * size);

    send(self(), "sort", null, arr);
  }

  @message
  public void sort(ActorName caller, int[] unsorted) throws RemoteCodeException {
    this.caller = caller;
    if (unsorted.length == 1) {
      if (caller == null) {
        return;
      } else
        send(caller, "merge", unsorted);
    }

    if (unsorted.length > 1) {
      int[] left = new int[unsorted.length / 2];
      int[] right = new int[unsorted.length - unsorted.length / 2];
      for (int i = 0; i < unsorted.length / 2; i++)
        left[i] = unsorted[i];
      for (int i = unsorted.length / 2; i < unsorted.length; i++)
        right[i - unsorted.length / 2] = unsorted[i];

      ActorName lhs = create(MsgMergesorter.class);
      ActorName rhs = create(MsgMergesorter.class);

      send(lhs, "sort", self(), left);
      send(rhs, "sort", self(), right);
    }
  }

  @message
  public void merge(int[] sorted) {
    received++;
    if (received == 1) {
      half = sorted;
      return;
    }

    if (received == 2) {
      int i;
      int lhsCount = 0;
      int rhsCount = 0;
      int[] merged = new int[sorted.length + half.length];
      for (i = 0; i < merged.length; i++) {
        if (sorted[rhsCount] < half[lhsCount])
          merged[i] = sorted[rhsCount++];
        else
          merged[i] = half[lhsCount++];
        if (lhsCount == half.length || rhsCount == sorted.length)
          break;
      }
      for (int j = lhsCount; j < half.length; j++)
        merged[++i] = half[j];
      for (int j = rhsCount; j < sorted.length; j++)
        merged[++i] = sorted[j];

      if (caller == null) {
        // System.out.print("Finish: ");
        // System.out.println(Arrays.toString(merged));
      } else
        send(caller, "merge", merged);
    }
  }

}
