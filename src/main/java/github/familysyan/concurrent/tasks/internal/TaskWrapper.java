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
	
	/**
	 * Returns true if this task is ready to be executed. Otherwise returns false.
	 * @return true/false
	 */
	public boolean isReady() {
		return ready;
	}
	
	/**
	 * Add dependency of this task.
	 * @param dependencyId The id of the dependency task.
	 */
	public void addDependency(String dependencyId) {
		dependencies.add(dependencyId);
		ready = false;
	}
	
	/**
	 * This method will be called when some dependency has successfully finished execution.
	 * @param dependencyId The id of the finished dependency.
	 * @param dependencyResult The execution result of the finished dependency.
	 */
	public void fulfillDependency(String dependencyId, Object dependencyResult) {
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
