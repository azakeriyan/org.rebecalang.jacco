package org.rebecalang.jacco.modelchecker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.swing.SwingWorker.StateValue;

import org.rebecalang.jacco.actor.Message;

public class RebecState {
	HashMap<String, LinkedList<Message>> queue = new HashMap<String, LinkedList<Message>>();
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
		
		for ( Entry<String, LinkedList<Message>> i : newState.getQueue().entrySet()){
			if  ((i.getValue() != null && queue.get(i.getKey()) == null ) || (i.getValue() == null && queue.get(i.getKey()) != null ) || (i.getValue() != null && queue.get(i.getKey()) != null && i.getValue().size() != queue.get(i.getKey()).size()))
				return false;
			Iterator<Message> newStateIterator = i.getValue().iterator();
			for(Message message : queue.get(i.getKey())) {
				Message newStateMessage = newStateIterator.next();
				if (!message.equals(newStateMessage))
					return false;
			}
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
	
	public HashMap<String, LinkedList<Message>> getQueue() {
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
//    	
        int result = queue != null ? queue.hashCode() : 0;
//        System.out.println("queue hash code : " + result);
        result = 31 * result + (stateVariables != null ? stateVariables.hashCode() : 0);
        
//        System.out.println("this section hash code :  "+result);
//          System.out.println("/*****************************************************/");
        return result;
    }
    
    public boolean qEmpty(){
    	if (queue.isEmpty())
    		return true;
    	for (LinkedList<Message> i : queue.values())
    		if (!i.isEmpty())
    			return false;
    	return true;
    }
}
