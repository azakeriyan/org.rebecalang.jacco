package diningPhils;

import org.rebecalang.javactor.actor.ReactiveSystem;

import osl.manager.Actor;
import osl.manager.ActorName;
import osl.manager.RemoteCodeException;
import osl.manager.annotations.message;

public class Driver extends Actor {
	
	private ActorName lphil,rphil;
	
	@message
	public void setUp() throws  RemoteCodeException{
		
		ActorName right = create(Fork.class);
		ActorName left	= create(Fork.class);
		
		lphil = create(DiningPhils.class,right , left);
		rphil = create(DiningPhils.class, right , left);
		
//		call(left, "setPhils", lphil,rphil);
//		call(right, "setPhils", rphil,lphil);
		
		((Fork)ReactiveSystem.getInstance().getActor(right.getId())).setPhils(lphil, rphil);
		((Fork)ReactiveSystem.getInstance().getActor(left.getId())).setPhils(rphil,lphil);
		send(lphil, "arrive");
		send(rphil, "arrive");
		
	}

}
