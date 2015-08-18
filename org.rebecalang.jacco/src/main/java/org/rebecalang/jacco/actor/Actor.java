package org.rebecalang.jacco.actor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.UUID;

import org.rebecalang.jacco.modelchecker.RebecState;


public abstract class Actor {
	protected String id;
	private HashMap<String, LinkedList<Message>> messageQueue;
	private int queueSize;

	protected abstract String printState();
	
	private boolean active;

	
	
	public Actor(String id , String message) {
		this.active = true;
		this.queueSize = 10;
		if (id != null)
			this.id = id;
		else
			this.id =new String(this.getClass().getName() + (UUID.randomUUID()).toString());
		messageQueue = new HashMap<String, LinkedList<Message>>();
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
		
		if (message != null){
			synchronized (messageQueue) {
				LinkedList<Message> q = new LinkedList<Message>();
				q.add(new Message(message, this));
				messageQueue.put(this.id, q);
			}
		}
			
	}

	//have some problems when you try to execute the actor code you have written. read the code. also a very bas written code. clean in pls!
	public void start() {
		new Thread() {
			public void run() {
				while (true) {
					try {
						String m = null;
						synchronized (messageQueue) {
							if (messageQueue.isEmpty())
								messageQueue.wait();	
							for ( Entry<String, LinkedList<Message>> i : messageQueue.entrySet())
								if (!i.getValue().isEmpty()){
									m = i.getKey();
									break;
								}
							if (m == null){
								messageQueue.wait();
								for ( Entry<String, LinkedList<Message>> i : messageQueue.entrySet())
									if (!i.getValue().isEmpty()){
										m = i.getKey();
										break;
									}
							}
							
						}
						execute(m);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (RemovedActorException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	 public RebecState getState() {
		RebecState retValue = new RebecState();
		
		for ( Entry<String, LinkedList<Message>> i:messageQueue.entrySet()){
			LinkedList<Message> q = new LinkedList<Message>();
			for (Message j : i.getValue())
				q.addLast(j);
			retValue.getQueue().put(i.getKey(), q);
//			System.out.println(message.getMessage() + " " + message.getArgs().length);
		}

		for (Field field : getClass().getDeclaredFields()) {
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
		
		for (Entry<String, LinkedList<Message>> i : rebecState.getQueue().entrySet()){
			LinkedList<Message> q = new LinkedList<Message>();
			for (Message j : i.getValue())
				q.addLast(j);
			messageQueue.put(i.getKey(), q);
			
		}
			


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

	public void  send(String message, Actor sender,Object... args) throws Exception {
		synchronized (messageQueue) {
			LinkedList<Message> q = messageQueue.get(sender.getId());
			if (q == null)
				q = new LinkedList<Message>();
			q.addLast(new Message(message, sender,args));
			messageQueue.put(sender.getId(), q);
			messageQueue.notify();
		}	
		if(messageQueue.size() > queueSize){
			throw new Exception("Queue Size Overflow");
		}
		
	}

	public void setId(String id) {
		if (messageQueue.get(this.id) != null){
			LinkedList<Message> q = new LinkedList<Message>(messageQueue.remove(this.id));
			messageQueue.put(id, q);
		}
		
		
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

	
	//this method no longer works properly. should be updated with the changes in the queuing of actor model
	public synchronized Message getNextMessage(LinkedList<Message> queue){
		Message m = null;
		if (queue.isEmpty()){
			System.out.println("empty message queue");
		}
		m =  queue.removeFirst();
		return m;
		
	}
	
	//the index string determines the actor form which the next message will be executed.
	public void execute(String index) throws RemovedActorException {
		if (active){
//			Message message = getNextMessage(messageQueue);
//			synchronized (messageQueue) {
//				if (message == null){
//					if (messageQueue.isEmpty())
//						System.out.println("empty message queue");
//					message = messageQueue.removeFirst();
//				}
				
//			}
			Message message = messageQueue.get(index).removeFirst();
				
			//remember this is not completely right because there can be methods with same name and different args.
			HashMap<String, Actor> before = new HashMap<String, Actor>(ReactiveSystem.getInstance().getMap());
			
			try {
				Method[] methods = this.getClass().getDeclaredMethods();
				for (int i = 0 ; i< methods.length ; i++){
					if (methods[i].getName().compareTo(message.getMessage()) == 0){
						try {
							methods[i].invoke(this, message.getArgs());
						} catch (Exception e) {
							e.printStackTrace();
							System.err.println(message.getArgs());
						}
						
					}
				}
//				System.out.println(this.printState());
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
//				System.err.println("error here");
				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			} catch (InvocationTargetException e) {
//				e.printStackTrace();
			}
			// }
			HashMap<String, Actor> after = new HashMap<String, Actor>(ReactiveSystem.getInstance().getMap());
			int counter = 0;
			for (String i : after.keySet()){
				if (before.get(i) == null){
//					System.out.println("changing a an actor name. new name : " + Integer.toString(message.hashCode()*31 + counter));
					Actor temp = ReactiveSystem.getInstance().getActor(i);
					ReactiveSystem.getInstance().removeActor(i);
					temp.setId(Integer.toString(message.hashCode()*31+counter));
					osl.manager.Actor.rename(i, Integer.toString(message.hashCode()*31+counter));
					ReactiveSystem.getInstance().addActor(temp);
					counter++;
				}
			}
			
		}
		else
			throw new RemovedActorException();
	}
	@Override
	public int hashCode() {
		
		int result =0;
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
		return this.id + " " + messageQueue.toString();
	}
	
	public void destruct(){
		this.active = false;
	}
	
	public int getQueueSize(){
		return queueSize;
	}
	
	public void setQueueSize(int s){
		queueSize = s;
	}
	
	
	
}