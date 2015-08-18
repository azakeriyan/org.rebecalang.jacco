package org.rebecalang.javactor.modelchecker;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.rebecalang.javactor.actor.Actor;
import org.rebecalang.javactor.actor.Message;
import org.rebecalang.javactor.actor.ReactiveSystem;

public class ModelChecker {
	Hashtable<Long, LinkedList<SystemState>> stateSpace = new Hashtable<Long, LinkedList<SystemState>>();
	private ArrayList<Integer> deadlockStates = new ArrayList<Integer>();

	public void bfs() {
		
//		System.out.println("starting Time");
		long time = System.currentTimeMillis();
		LinkedList<SystemState> openStates = new LinkedList<SystemState>();
		
		SystemState newState = new SystemState();
		for (Actor actor : ReactiveSystem.getInstance().getActors()) {
//			System.out.println(actor.getId() + " " + actor.getState().getQueue().size());
			newState.getRebecStates().put(actor.getId(),
					actor.getState());
		}
		
		openStates.addLast(newState);
		newState.setId(SystemState.getNewStateID());
		
//		System.out.println("-----------------new State value -------------------------");
//		for (Entry<String, RebecState> index :newState.getRebecStates().entrySet()){
//			System.out.println(index.getKey() + " : " );
//			System.out.println("queue size : "+index.getValue().getQueue().size());
//			System.out.println("state value" + index.getValue().getStateVariables());
//		}
//		System.out.println("---------------------------------------------------------------");
		
//		
		
		int a = 0;
		boolean deadlock;
//		System.out.println("digraph html {");
		while (!openStates.isEmpty()) {
//			System.out.println("state counter "+ a++);
			SystemState state = openStates.removeFirst();
			int currentStateID = state.getId();
//			if (currentStateID == 100)
//				System.exit(0);
//			System.out.println("-----------------new State value -------------------------");
//			System.out.println("state id:" + state.getId());
//			System.out.println("state hash code : " + state.hashCode());
//			for (Entry<String, RebecState> index :state.getRebecStates().entrySet()){
//				System.out.println(index.getKey() + " : ");
//				System.out.println("queue size : "+index.getValue().getQueue().size());
//				for (Message i :index.getValue().getQueue()){
//					System.out.println("message : "  + i.getMessage());
//				}
//				System.out.println("state value" + index.getValue().getStateVariables());
//			}
//			System.out.println("---------------------------------------------------------------");
			
			deadlock = true;
			for (Entry<String, RebecState> entry : state.getRebecStates()
					.entrySet()) {
				if (entry.getValue().getQueue().isEmpty()){
//					System.out.println(entry.getKey() + " queue is empty");
					continue;
				}
				deadlock = false;
				ReactiveSystem.getInstance().deactivateAll();
				for (Entry<String, RebecState> entry2 : state.getRebecStates()
						.entrySet()) {
					ReactiveSystem.getInstance().getActor(entry2.getKey())
							.restoreState(entry2.getValue());
					//activating only relevant actors for this execution 
					ReactiveSystem.getInstance().getActor(entry2.getKey()).activate();
				}
				
//				System.out.println("running a call from " + entry.getKey());
//				System.out.println(ReactiveSystem.getInstance().getActor(entry.getKey()));
				ReactiveSystem.getInstance().getActor(entry.getKey()).execute();
//				System.out.println(entry.getKey() + " has executed ");
				newState = new SystemState();
				for (Actor actor: ReactiveSystem.getInstance().getActors()){
					if (actor.isActive())
						newState.getRebecStates().put(
								actor.getId(),actor.getState());
				}
				newState.setParentId(currentStateID);
				long hashCode = newState.hashCode();
//				System.out.println("generated hash code : " + hashCode);
				LinkedList<SystemState> similarStates = stateSpace.get(hashCode);
				boolean found = false;
				int foundStateId = 0;
				if (similarStates != null)
					for (SystemState similarState : similarStates) {
						if (similarState.equals(newState)) {
							found = true;
							foundStateId = similarState.getId();
//							System.out.println("found a similar state");
							break;
						}
					}
				if (!found) {
					if (similarStates == null) {
						similarStates = new LinkedList<SystemState>();
						stateSpace.put(hashCode, similarStates);
					}
//					System.out.println("adding a new state to open state queue " );
					similarStates.addLast(newState);
					openStates.addLast(newState);
					newState.setId(SystemState.getNewStateID());
					foundStateId = newState.getId();
				}
//				System.out.println("s" + currentStateID + 
//						"-> s"+ foundStateId +"[label=\"" + entry.getKey() + "." + entry.getValue().getQueue().getFirst().getMessage() + "\"];" );
			}
			if (deadlock)
				deadlockStates.add(currentStateID);
				
		}
//		System.out.println("}");
		time = System.currentTimeMillis() -time;
		System.out.println("states created : " + (SystemState.getNewStateID() -1 ) );
		System.out.println("execution time : " + time + " ms");
	}
	
	
	public void printDeadlocks(){
		System.out.println("printing deadLocks");
		for (int i:deadlockStates){
			int parent = i;
			while (parent != 0){
				System.out.println(parent);
				parent = findParent(parent);
			}
		}
		System.out.println("---------------------");
	}
	
	private int findParent(int stateId){
		for (Entry<Long, LinkedList<SystemState>> entry : stateSpace.entrySet()){
			for (SystemState index : entry.getValue()){
				if (index.getId() == stateId){
					return index.getParentId();
				}
			}
		}
		return 0;
	}
	
}
