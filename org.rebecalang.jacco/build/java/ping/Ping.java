package ping;

import org.rebecalang.javactor.actor.Exclude;

import osl.manager.Actor;
import osl.manager.ActorName;
import osl.manager.annotations.message;

public class Ping extends Actor{
	
	/**
	 * 
	 */
	@Exclude
	private static final long serialVersionUID = 3233695359126360164L;
	
	
	private ActorName pong;
	
	@message
	public void setPong(ActorName pong){
		this.pong = pong;
	}
	
	@message
	public void ping(){
		send(pong, "pong");
	}

}
