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

import java.util.Random;

import org.rebecalang.javactor.actor.Exclude;

import osl.manager.*;
import osl.manager.annotations.*;

/**
 * This class is an actor-based implementation of a quick sort algorithm.
 * 
 * @author Bobak Hadidi (bhadidi2@illinois.edu)
 * 
 */
public class MsgQuicksorter extends Actor {

	@Exclude
  private static final long serialVersionUID = 895392L;

  private int pings;
  private ActorName caller;
  private int[] half;

  public MsgQuicksorter() {
    pings = 0;
  }

  @message
  public void boot(Integer size) throws RemoteCodeException {
    if (size < 1) {
      System.out.println("Enter int > 0");
      return;
    }
    Random rn = new Random(19 + size);
    int[] arr = new int[size];

    for (int i = 0; i < size; i++)
      arr[i] = rn.nextInt(100);

    ActorName sorter = create(MsgQuicksorter.class);
    send(sorter, "sort", null, arr);
  }

  @message
  public void sort(ActorName caller, int[] unsorted) throws RemoteCodeException {
    this.caller = caller;
    // prevents inf. loop iff unsorted is homogeneous
    boolean allRepeats = false;
    if (unsorted.length > 1) {
      int prev = unsorted[0];
      for (int i = 1; i < unsorted.length; i++) {
        if (unsorted[i] != prev)
          break;
        if (i == unsorted.length - 1)
          allRepeats = true;
      }
    }
    if (unsorted.length <= 1 || allRepeats) {
      if (caller == null) {
        // print if boot input 1
        // System.out.println("Finish:");
        // System.out.println(java.util.Arrays.toString(unsorted));
        return;
      } else
        send(caller, "done", unsorted);
    }

    else if (unsorted.length > 1) {
      int pivot = unsorted[0];
      int[] middle = getEqual(pivot, unsorted);
      int[] right = getLarger(pivot, unsorted);
      int[] left = getLess(pivot, unsorted);
      // if only 2 unique ints, either right or left will be empty
      // pass middle as substitute in that case
      if (right.length == 0)
        right = middle;
      else if (left.length == 0)
        left = middle;
      // otherwise add middle to one
      else {
        if (right.length <= left.length)
          right = append(right, middle);
        else
          left = append(left, middle);
      }

      ActorName lh = create(MsgQuicksorter.class);
      ActorName rh = create(MsgQuicksorter.class);
      send(lh, "sort", self(), left);
      send(rh, "sort", self(), right);
    }
  }

  @message
  public void done(int[] sorted) {
    pings++;
    if (pings == 1)
      half = sorted;
    else if (pings == 2) {
      int[] full = new int[sorted.length + half.length];

      if (half.length == 0)
        full = sorted;
      else if (sorted.length == 0)
        full = half;
      else if (half[0] > sorted[0]) {
        for (int i = 0; i < sorted.length; i++)
          full[i] = sorted[i];
        for (int i = 0; i < half.length; i++)
          full[i + sorted.length] = half[i];
      } else if (half[0] < sorted[0]) {
        for (int i = 0; i < half.length; i++)
          full[i] = half[i];
        for (int i = 0; i < sorted.length; i++)
          full[i + half.length] = sorted[i];
      }

      if (caller == null) {
        // print
        // System.out.println("Finish:");
        // System.out.println(java.util.Arrays.toString(full));
        // print
        return;
      }

      else
        send(caller, "done", full);
    }
  }

  private int[] getEqual(int pivot, int[] unsorted) {
    java.util.LinkedList<Integer> list = new java.util.LinkedList<Integer>();
    for (int e : unsorted) {
      if (e == pivot)
        list.add(e);
    }
    int[] a = new int[list.size()];
    for (int i = 0; i < a.length; i++) {
      a[i] = list.get(i);
    }
    return a;
  }

  private int[] getLarger(int pivot, int[] unsorted) {
    java.util.LinkedList<Integer> list = new java.util.LinkedList<Integer>();
    for (int e : unsorted) {
      if (e > pivot)
        list.add(e);
    }
    int[] a = new int[list.size()];
    for (int i = 0; i < a.length; i++) {
      a[i] = list.get(i);
    }
    return a;
  }

  private int[] getLess(int pivot, int[] unsorted) {
    java.util.LinkedList<Integer> list = new java.util.LinkedList<Integer>();
    for (int e : unsorted) {
      if (e < pivot)
        list.add(e);
    }
    int[] a = new int[list.size()];
    for (int i = 0; i < a.length; i++) {
      a[i] = list.get(i);
    }
    return a;
  }

  private int[] append(int[] base, int[] middle) {
    int[] retVal = new int[base.length + middle.length];
    int i = 0;
    for (; i < base.length; i++)
      retVal[i] = base[i];
    for (int j = 0; j < middle.length; j++)
      retVal[i++] = middle[j];
    return retVal;
  }

}
