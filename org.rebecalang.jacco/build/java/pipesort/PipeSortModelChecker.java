package pipesort;

import org.rebecalang.javactor.actor.Actor;
import org.rebecalang.javactor.modelchecker.ModelChecker;

public class PipeSortModelChecker {
	
	public static void main(String[] args) {
		
		Actor boot = new Driver();
		new ModelChecker().bfs();
	}

}
