package diningPhils;

import osl.manager.Actor;
import osl.manager.ActorName;
import osl.manager.annotations.message;


public class Fork extends Actor {

	private boolean rRequest,lRequest,lAssign,rAssign;
	
	ActorName lPhilName,rPhilName;
	
	public Fork() {
		
		this.lAssign = false;
		this.rAssign = false;
		this.lRequest = false;
		this.rRequest = false;
	}
	
	@message
	public void setPhils(ActorName lphil , ActorName rphil){
		this.lPhilName = lphil;
		this.rPhilName = rphil;
	}


	
	public void request(ActorName sender){
		//System.out.println(id + " is running : request and left Phil is : " + lPhilName +  " " + message.getSender().getId());
		if (lPhilName.equals(sender)){
			if (!lRequest){
				lRequest = true;
				if (!rAssign){
					lAssign = true;
					send(lPhilName, "permit",self());
				}
			}
		}
		else{
//			//System.out.println("in else part");
			if (!rRequest){
				rRequest = true;
				if (!lAssign){
					rAssign = true;
					send(rPhilName, "permit",self());
				}
			}	
		}
			
	}
	
	public void release (ActorName sender){
		//System.out.println(id + " is running : release and left Phil is : " + lPhilName );
		if (lPhilName.equals(sender) && lAssign){
			lRequest = false;
			lAssign = false;
			if (rRequest){
				rAssign = true;
				send(rPhilName, "permit",self());
			}
		}
		if (rPhilName.equals(sender) && rAssign){
			rRequest = false;
			rAssign = false;
			if (lRequest){
				lAssign = true;
				send(lPhilName, "permit",self());
			}
		}
	}

}
