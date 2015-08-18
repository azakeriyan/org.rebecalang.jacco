package org.rebecalang.jacco.actor;

import java.util.Collection;
import java.util.HashMap;


public class ReactiveSystem {

	private HashMap<String, Actor> actorSystem;
	private static ReactiveSystem instance = new ReactiveSystem();
	
	private ReactiveSystem(){
		this.actorSystem = new HashMap<String, Actor>();
		
	}
	
	public static ReactiveSystem getInstance(){
		return instance;
	}
	
	public void addActor(Actor actor){
		this.actorSystem.put(actor.getId(), actor);
	}
	
	public Actor getActor (String name){
		return this.actorSystem.get(name);
	}

	public Collection<Actor> getActors() {
		return actorSystem.values();
	}
	
	public HashMap<String, Actor> getMap(){
		return this.actorSystem;
	}
	
	public void start() {
		for (Actor index : actorSystem.values() ){
			index.start();
		}
	}
	
	public void deactivateAll(){
		for (String i : actorSystem.keySet()){
			actorSystem.get(i).deactivate();
		}
	}
	
	public void removeActor (String i){
		actorSystem.remove(i);
	}
}
