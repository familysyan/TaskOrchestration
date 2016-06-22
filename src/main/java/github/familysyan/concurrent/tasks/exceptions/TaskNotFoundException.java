package github.familysyan.concurrent.tasks.exceptions;

public class TaskNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3197028069696950499L;
	
	public TaskNotFoundException() {
		
	}
	
	public TaskNotFoundException(String taskId) {
		super("The task of id " + taskId + " is not found");
	}

}
