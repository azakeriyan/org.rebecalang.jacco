package org.rebecalang.jacco.modelchecker;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.rebecalang.jacco.actor.Actor;
import org.rebecalang.jacco.actor.Message;
import org.rebecalang.jacco.actor.ReactiveSystem;
import org.rebecalang.jacco.actor.RemovedActorException;

public class ModelChecker {
	Hashtable<Long, LinkedList<SystemState>> stateSpace = new Hashtable<Long, LinkedList<SystemState>>();
	private ArrayList<Integer> deadlockStates = new ArrayList<Integer>();

	public void bfs() throws RemovedActorException {
				
//		System.out.println("starting Time");
		long time = System.currentTimeMillis();
		LinkedList<SystemState> openStates = new LinkedList<SystemState>();
		
		SystemState newState = new SystemState(-1);
		
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
//			for (Entry<String, Integer> index :state.getRebecStates().entrySet()){
//				System.out.println(ReactiveSystem.getInstance().getActor(index.getKey()).getClass().toString() + " ---- " + index.getKey() + " : ");
//				for ( Entry<String, LinkedList<Message>> m : state.getState(index.getKey()).getQueue().entrySet()){
//					System.out.println(m.getKey() + " := " + m.getValue());
//				}
//				System.out.println(state.getState(index.getKey()).getStateVariables().toString());
//				System.out.println("***************************");
//			}
//			System.out.println("---------------------------------------------------------------");
			
			deadlock = true;
			for (String i: state.getRebecStates().keySet()) {
				
				
				
				if (state.getState(i) == null){
					System.out.println("null state presented");
				}
				if (state.getState(i).qEmpty()){
//					System.out.println(entry.getKey() + " queue is empty");
					continue;
				}
				
				deadlock = false;
				for ( Entry<String, LinkedList<Message>> k : state.getState(i).getQueue().entrySet()){
					if (!k.getValue().isEmpty()){
						ReactiveSystem.getInstance().deactivateAll();
						for (String j : state.getRebecStates().keySet()) {
							ReactiveSystem.getInstance().getActor(j)
									.restoreState(state.getState(j));
							//activating only relevant actors for this execution 
							ReactiveSystem.getInstance().getActor(j).activate();
						}
						
			//				System.out.println("running a call from " + entry.getKey());
			//				System.out.println(ReactiveSystem.getInstance().getActor(entry.getKey()));
					
							ReactiveSystem.getInstance().getActor(i).execute(k.getKey());
					
			//				System.out.println(entry.getKey() + " has executed ");
						newState = new SystemState(currentStateID);
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
						System.out.println("s" + currentStateID + 
								"-> s"+ foundStateId +"[label=\"" + i + "." + k.getValue().getFirst().getMessage() + "\"];" );
					}
				}
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
