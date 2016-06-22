package github.familysyan.concurrent.tasks.exceptions;

public class RedundantTaskException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7031529632969781043L;
	
	public RedundantTaskException() {
		
	}
	
	public RedundantTaskException(String taskId) {
		super("The task of id " + taskId + " has already been submitted once.");
	}

}
