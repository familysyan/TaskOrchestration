package github.familysyan.concurrent.tasks;

import java.util.List;

public interface Task<E>{
	
	public String getUniqueTaskId();
	
	public E execute(List<Object> dependencies);

}
