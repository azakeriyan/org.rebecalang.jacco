package fibonacci;

import org.rebecalang.javactor.actor.Actor;
import org.rebecalang.javactor.modelchecker.ModelChecker;

public class FibModelChecker {
	
	public static void main(String[] args) {
		
		Actor boot = new Driver();
		new ModelChecker().bfs();
	}

}
