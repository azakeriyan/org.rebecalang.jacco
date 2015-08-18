package org.rebecalang.javactor.actor;

import java.util.Arrays;


public class Message {
	
	private String message;
	private Actor sender;
	private Object[] args;
	
	public Message (String message,Actor sender,Object... args){
		

		this.message = message;
		this.sender = sender;
		this.args = args;
	}
	
	public void setMessage(String message){
		this.message = message;
	}
	
	
	public Actor getSender() {
		return sender;
	}

	public void setSender(Actor sender) {
		this.sender = sender;
	}

	public String getMessage(){
		return this.message;
	}

	@Override
	public boolean equals(Object newMessage) {
		if (!(newMessage instanceof Message))
			return false;
		Message message = (Message) newMessage;
		if (!(message.message.equals(this.message) && message.sender.getId().equals(this.sender.getId())))
			return false;
		if ((args == null && message.getArgs()!=null ) || (args!=null && message.getArgs() == null))
			return false;
		else if (args != null && message.getArgs()!= null)
			for (int i =0;i<args.length;i++){
				if (!(args[i].equals(message.getArgs()[i])))
					return false;
			}
		return true;
	}

    @Override
    public int hashCode() {
        int result = message != null ? message.hashCode() + 1 : 1;
        int res =1;
        if (args != null && args.length!=0){
        	for (int i = 0 ;i <args.length ; i++ ){
        		res =31*(res + (args[i] == null ? 0 :args[i].hashCode()));
        	}
        }
        result = 31 * result  + 31*res +  (sender != null ? sender.getId().hashCode() : 0);
        return result;
    }
    
    public Object[] getArgs() {
		return args;
	}
    
    public void setArgs(Class<?>... args) {
		this.args = args;
	}
    
    public String toString(){
    	return message + " " + sender.getId() + " " + Arrays.toString(args);
    }
}
