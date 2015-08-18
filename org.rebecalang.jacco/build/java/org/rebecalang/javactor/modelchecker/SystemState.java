package org.rebecalang.javactor.modelchecker;

import java.util.HashMap;

public class SystemState {
	private static int stateCounter = 0;
	
	int id;
	
	private int parentId;
	
	HashMap<String, RebecState> rebecStates = new HashMap<String, RebecState>();

	public static int getNewStateID() {
		return stateCounter++;
	}
	
	@Override
	public boolean equals(Object newSystemStateObject) {
		if (!(newSystemStateObject instanceof SystemState))
			return false;
		SystemState newSystemState = (SystemState) newSystemStateObject;
		for (String id : rebecStates.keySet()) {
			if (!rebecStates.get(id).equals(newSystemState.getRebecStates().get(id)))
				return false;
		}
		return true;
	}
	
	public HashMap<String, RebecState> getRebecStates() {
		return rebecStates;
	}

    @Override
    public int hashCode() {
        return rebecStates != null ? rebecStates.hashCode() : 0;
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

