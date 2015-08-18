package osl.tests.manager.actors;

import kilim.pausable;
import osl.util.LSCQueue; 
import osl.manager.ActorMsgRequest;
import osl.manager.ActorExecutor;

public class SenderActorExecutor extends osl.manager.ActorExecutor{
	private SenderActor actor;  
	
	public SenderActorExecutor(SenderActor a, LSCQueue<Object> q) {
		super(a, q);
		this.actor = a;
	}
	
	@pausable
	public Object execute(ActorMsgRequest msgRequest) throws Exception {
		String msgName = msgRequest.method;
		Object[] args = msgRequest.methodArgs;
		boolean typeOK = false;


//////////// inherited methods










		return super.execute(msgRequest);
	}

}




