package org.rebecalang.javactor.actor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.rebecalang.javactor.modelchecker.RebecState;

public abstract class Actor {
	protected String id;
	private LinkedList<Message> messageQueue;

	protected abstract String printState();
	
	private boolean active;

	
	
	public Actor(String id , String message) {
		this.active = true;
		if (id != null)
			this.id = id;
		else
			this.id =new String(this.getClass().getName() + (UUID.randomUUID()).toString());
		messageQueue = new LinkedList<Message>();
		ReactiveSystem.getInstance().addActor(this);
		
		if (message == null){
			Method[] meth =  getClass().getDeclaredMethods();
			  for ( int i = 0 ; i< meth.length;i++){
				  if(meth[i].getName().compareTo("setUp")== 0){
					  message = "setUp";
					  break;
				  }
			  }
		}
			
		if (message!=null)
			synchronized (messageQueue) {
				messageQueue.add(new Message(message, this));
			}
			
	}


	public void start() {
		new Thread() {
			public void run() {
				while (true) {
					try {
						synchronized (messageQueue) {
							if (messageQueue.isEmpty())
								messageQueue.wait();
						}
						execute();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public RebecState getState() {
		RebecState retValue = new RebecState();

		for (Message message : messageQueue){
			retValue.getQueue().addLast(message);
//			System.out.println(message.getMessage() + " " + message.getArgs().length);
		}

		for (Field field : getClass().getDeclaredFields()) {
//			boolean exclude = false;
//			Annotation[] annotation = field.getDeclaredAnnotations();
//			for (int i = 0 ; i < annotation.length ; i++){
//				System.out.println("annotation : " + annotation[i].getClass().toString()  + " " + Exclude.class.toString());
//				if (annotation[i]== Exclud) {
//					System.out.println("field #" + field.getName() + "# is annotated exclude");
//					exclude = true;
//					break;
//				}	
//			}
			if (!field.isAnnotationPresent(Exclude.class)){
				try {
					if (!field.isAccessible())
						field.setAccessible(true);
					retValue.getStateVariables().put(field.getName(),
							field.get(this));
//					System.out.println("add field to state : " + field.getName());
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return retValue;
	}

	public void restoreState(RebecState rebecState) {
		messageQueue.clear();
		for (Message message : rebecState.getQueue())
			messageQueue.addLast(message);


		for(String index :rebecState.getStateVariables().keySet()){
			try {
//				System.out.println("state variables : " + index);
				Field field = getClass().getDeclaredField(index);
				if (!field.isAccessible())
					field.setAccessible(true);
				field.set(this,rebecState.getStateVariables().get(index));
			} catch (NoSuchFieldException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException  e) {
				e.printStackTrace();
			}
			
		}
		
	}

	public void send(String message, Actor sender,Object... args) {
		synchronized (messageQueue) {
			messageQueue.addLast(new Message(message, sender,args));
			messageQueue.notify();
		}
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof Actor))
			return false;
		return id.equals(((Actor) arg0).id);
	}

	public void execute() {
		Message message;
		synchronized (messageQueue) {
			if (messageQueue.isEmpty())
				System.out.println("empty message queue");
			message = messageQueue.removeFirst();
		}
		//remember this is not completely right because there can be methods with same name and different args.
		HashMap<String, Actor> before = new HashMap<String, Actor>(ReactiveSystem.getInstance().getMap());
		
		try {
			Method[] methods = this.getClass().getDeclaredMethods();
			for (int i = 0 ; i< methods.length ; i++){
				if (methods[i].getName().compareTo(message.getMessage()) == 0){
					methods[i].invoke(this, message.getArgs());
				}
			}
//			System.out.println(this.printState());
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		// }
		HashMap<String, Actor> after = new HashMap<String, Actor>(ReactiveSystem.getInstance().getMap());
		int counter = 0;
		for (String i : after.keySet()){
			if (before.get(i) == null){
//				System.out.println("changing a an actor name. new name : " + Integer.toString(message.hashCode()*31 + counter));
				Actor temp = ReactiveSystem.getInstance().getActor(i);
				ReactiveSystem.getInstance().removeActor(i);
				temp.setId(Integer.toString(message.hashCode()*31+counter));
				osl.manager.Actor.rename(i, Integer.toString(message.hashCode()*31+counter));
				ReactiveSystem.getInstance().addActor(temp);
				counter++;
			}
		}
		

	}
	@Override
	public int hashCode() {
		
		int result =id.hashCode();
		for (Field field:this.getClass().getDeclaredFields()){
			result += field.hashCode();
		}
		result = 31* result +(messageQueue!=null ? messageQueue.hashCode() : 0);
	
		return result;
	}
	
	public void activate(){
		this.active = true;
	}
	
	public void deactivate(){
		this.active = false;
	}
	
	public boolean isActive(){
		return this.active;
	}

	@Override
	public String toString(){
		return Arrays.toString(messageQueue.toArray());
	}
	
}