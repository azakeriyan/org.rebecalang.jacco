package org.rebecalang.jacco.actor;

public class RemovedActorException extends Exception {
	public RemovedActorException(){
		super("This actor has been previously removed");
	}
}
