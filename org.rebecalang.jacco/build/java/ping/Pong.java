package ping;

import osl.manager.Actor;
import osl.manager.ActorName;

public class Pong extends Actor {
	
	private ActorName ping;
	
	public Pong(ActorName ping){
		this.ping = ping;
	}
	
	public void pong (){
//		System.out.println("pong");
		send(ping, "ping");
	}

	
	
	

}
