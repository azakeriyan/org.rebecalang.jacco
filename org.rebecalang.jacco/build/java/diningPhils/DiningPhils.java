package diningPhils;

import osl.manager.Actor;
import osl.manager.ActorName;


public class DiningPhils extends Actor{
	
	private boolean eating,lFork,rFork;
	
	ActorName rForkName , lForkName;
	
	public DiningPhils(ActorName leftFork,ActorName rightFork) {
		this.eating = false;
		this.lFork = false;
		this.rFork = false;
		
		this.rForkName = rightFork;
		this.lForkName = leftFork;
	}


	
	
	public void arrive (){
		//System.out.println(id + " is running : arrive" );
		send(lForkName, "request", self());
	}
	
	public void permit (ActorName sender){
		//System.out.println(id + " is running : permit and left fork name is : " + lForkName + " " + message.getSender().getId());
		if (lForkName.equals(sender)){
			if (!lFork){
				lFork = true;
				send(rForkName, "requset", self());
			}
		}
		else{
			if (lFork && (!rFork)){
				rFork = true;
				send(self(), "eat");
			}
		}
	}
	
	
	public void eat (){
//		System.out.println(id + " is running : eat" );
		this.eating = true;
		send(self(), "leave");
		this.send("leave",this);
	}
	
	public void leave(){
		//System.out.println(id + " is running : leave" );
		lFork = false;
		rFork = false;
		eating = false;
		
		send(rForkName, "release", self());
		send(lForkName, "release", self());
		send(self(), "arrive");
	}
}
