package github.familysyan.concurrent.tasks.exceptions;

public class ExecutorDownException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -183167757758660722L;
	
	public ExecutorDownException () {
		super("ExecutorService has been shutdown.");
    }

}
