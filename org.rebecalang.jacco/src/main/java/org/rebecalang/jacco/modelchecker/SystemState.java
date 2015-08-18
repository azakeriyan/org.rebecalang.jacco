package org.rebecalang.jacco.modelchecker;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.rebecalang.jacco.actor.Actor;
import org.rebecalang.jacco.actor.ReactiveSystem;

public class SystemState {
	private static int stateCounter = 0;
	
	private static HashMap<Integer, RebecState> allStates = new HashMap<Integer, RebecState>();
	
	int id;
	
	private int parentId;
	
	LinkedHashMap<String, Integer> rebecStates = new LinkedHashMap<String, Integer>();
	
	public SystemState(int parentId){
		if (parentId != -1)
			this.parentId = parentId;
		
		// creating hash map
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (Actor actor: ReactiveSystem.getInstance().getActors()){
			if (actor.isActive()){
				RebecState state = actor.getState();
				RebecState similar = allStates.get(state.hashCode());
				if (similar !=  null){
					if (similar.equals(state)){
						map.put(actor.getId(), state.hashCode());
					}
					else{
						int counter = 0 ;	
						while (true){
							counter++;
							similar = allStates.get(state.hashCode() + counter);
							if (similar == null){
								allStates.put(state.hashCode()+ counter, state);
								map.put(actor.getId(), state.hashCode() + counter);
								break;
							}
							else if(similar.equals(state)){
									map.put(actor.getId(), state.hashCode() + counter);
									break;
							}
						}
					}
				}
				else {
					allStates.put(state.hashCode(), state);
					map.put(actor.getId(), state.hashCode());
				}
				
			}
				
		}
		
		
		//sorting hash map
		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(map.entrySet());
        Collections.sort(list, new Comparator<Entry<String, Integer>>()
        {
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2)
            {
            	return o2.getValue().compareTo(o1.getValue());
            }
        });

        for (Entry<String, Integer> entry : list)
        {
            rebecStates.put(entry.getKey(), entry.getValue());
        }
        

        
	}

	
/*	public void addRebecState(String id ,RebecState state){
		RebecState similar = allStates.get(state.hashCode());
		if (similar !=  null){
			if (similar.equals(state)){
				rebecStates.put(id, state.hashCode());
			}
			else{
				int counter = 0 ;	
				while (true){
					counter++;
					similar = allStates.get(state.hashCode() + counter);
					if (similar == null){
						allStates.put(state.hashCode()+ counter, state);
						rebecStates.put(id, state.hashCode() + counter);
						break;
					}
					else if(similar.equals(state)){
							rebecStates.put(id, state.hashCode() + counter);
							break;
					}
				}
			}
		}
		else {
			allStates.put(state.hashCode(), state);
			rebecStates.put(id, state.hashCode());
		}
		
	}
	*/
	
	public RebecState getState(String id){
		return allStates.get(rebecStates.get(id));
	}
	
	public static int getNewStateID() {
		return stateCounter++;
	}
	
	@Override
	public boolean equals(Object newSystemStateObject) {
		if (!(newSystemStateObject instanceof SystemState))
			return false;
		SystemState newSystemState = (SystemState) newSystemStateObject;
		if (newSystemState.getRebecStates().size() != rebecStates.size())
			return false;
		Iterator<Integer> iterator = rebecStates.values().iterator(); 
		for(Integer i : newSystemState.getRebecStates().values())
			if(!iterator.next().equals(i))
				return false;
		
		return true;
	}
	
//	public HashMap<String, RebecState> getRebecStates() {
//		HashMap<String, RebecState> temp = new HashMap<String, RebecState>();
//		for (String i : rebecStates.keySet()){
//			temp.put(i, allStates.get(rebecStates.get(i)));
//		}
//		return temp;
//	}
	
	public HashMap<String, Integer> getRebecStates(){
		return this.rebecStates;
	}

    @Override
    public int hashCode() {
    	int res = 0;
    	for (int i : rebecStates.values())
    		res += i;
        return res;
    }
    
    public int getId() {
		return id;
	}
    public void setId(int id) {
		this.id = id;
	}
    
    public void setParentId(int parentId) {
		this.parentId = parentId;
	}
    
    public int getParentId() {
		return parentId;
	}
    
}

