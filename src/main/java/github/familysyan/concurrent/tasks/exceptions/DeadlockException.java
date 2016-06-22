package github.familysyan.concurrent.tasks.exceptions;

public class DeadlockException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4926624906209273707L;

	public DeadlockException () {
		super("Some of your submitted tasks have mutual dependency.");
    }


}
