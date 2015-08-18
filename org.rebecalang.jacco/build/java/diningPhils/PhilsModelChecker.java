package diningPhils;

import org.rebecalang.javactor.actor.Actor;
import org.rebecalang.javactor.modelchecker.ModelChecker;

public class PhilsModelChecker {
	
	public static void main(String[] args) {
		Actor boot = new Driver();
		new ModelChecker().bfs();
	}

}
