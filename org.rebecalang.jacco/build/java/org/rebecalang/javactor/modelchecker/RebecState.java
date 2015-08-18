package org.rebecalang.javactor.modelchecker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.SwingWorker.StateValue;

import org.rebecalang.javactor.actor.Message;

public class RebecState {
	LinkedList<Message> queue = new LinkedList<Message>();
	HashMap<String, Object> stateVariables = new HashMap<String, Object>();
	
	@Override
	public boolean equals(Object newStateObject) {
		if (!(newStateObject instanceof RebecState))
			return false;
		
		RebecState newState = (RebecState) newStateObject;
		if (newState.queue.size() != queue.size())
			return false;
		if (newState.stateVariables.size() != stateVariables.size())
			return false;
		
		Iterator<Message> newStateIterator = newState.queue.iterator();
		for(Message message : queue) {
			Message newStateMessage = newStateIterator.next();
			if (!message.equals(newStateMessage))
				return false;
		}
		
		
		for(String id : stateVariables.keySet()) {
			if ((stateVariables.get(id) == null && newState.getStateVariables().get(id) != null) || 
					(stateVariables.get(id) != null && newState.getStateVariables().get(id) == null))
				return false;
			if (stateVariables.get(id) != null && !stateVariables.get(id).equals(newState.stateVariables.get(id)))
				return false;
		}
		
		return true;
	}
	
	public LinkedList<Message> getQueue() {
		return queue;
	}
	
	public HashMap<String, Object> getStateVariables() {
		return stateVariables;
	}

    @Override
    public int hashCode() {
//    	System.out.println("/****************************************************/");
//    	System.out.println("printing hash code parts!! ");
//    	System.out.println("queue:");
//    	for (Message m :queue){
//    		System.out.println(m.getMessage() + "  " +Arrays.toString(m.getArgs()) + "  " + m.getSender().getId());
//    	}
//    	System.out.println("state vars:");
//    	for (String i : stateVariables.keySet()){
//    		System.out.println(i + " : " + stateVariables.get(i));
//    	}
    	
        int result = queue != null ? queue.hashCode() : 0;
//        System.out.println("queue hash code : " + result);
        result = 31 * result + (stateVariables != null ? stateVariables.hashCode() : 0);
        
//        System.out.println("this section hash code :  "+result);
//        System.out.println("/*****************************************************/");
        return result;
    }
}
