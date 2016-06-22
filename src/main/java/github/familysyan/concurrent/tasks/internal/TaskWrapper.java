package github.familysyan.concurrent.tasks.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.Callable;

import github.familysyan.concurrent.tasks.Task;

@SuppressWarnings("rawtypes")
public class TaskWrapper extends Observable implements Callable{
	
	private Task task;
	private Set<String> dependencies = new HashSet<String>();
	private List<Object> dependencyResults = new ArrayList<Object>();
	private boolean ready = true;
	
	public TaskWrapper(Task<?> task) {
		this.task = task;
	}
	
	public Task getTask() {
		return task;
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public void addDependency(String dependencyId) {
		dependencies.add(dependencyId);
		ready = false;
	}
	
	public void dependencyReady(String dependencyId, Object dependencyResult) {
		dependencies.remove(dependencyId);
		dependencyResults.add(dependencyResult);
		if (dependencies.isEmpty()) {
			ready = true;
		}
	}

	@SuppressWarnings("unchecked")
	public Object call() throws Exception {
		Object result = task.execute(dependencyResults);
		setChanged();
		notifyObservers(result);
		return result;
	}
	
	

	

}
