package ping;

import org.rebecalang.javactor.actor.ReactiveSystem;

import osl.manager.Actor;
import osl.manager.ActorName;
import osl.manager.RemoteCodeException;
import osl.manager.annotations.message;

public class Driver extends Actor{
	
	private ActorName ping;
	@message
	public void setUp() throws  RemoteCodeException{
		ping= create(Ping.class);
		ActorName pong = create(Pong.class,ping);
		//call(ping, "setPong",pong);
		((Ping)ReactiveSystem.getInstance().getActor(ping.getId())).setPong(pong);
		
		send(ping, "ping");
	}
	

}
