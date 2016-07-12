package github.familysyan.concurrent.tasks.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import github.familysyan.concurrent.tasks.Task;

@SuppressWarnings("rawtypes")
public class TaskWrapper extends InternalTask{
	
	private Task task;
	private Set<String> dependencies = new HashSet<String>();
	private List<Object> dependencyResults = new ArrayList<Object>();
	private boolean ready = true;
	private boolean notifyOthers = true;
	
	public TaskWrapper(Task<?> task) {
		this(task, true);
	}
	
	public TaskWrapper(Task<?> task, boolean notifyOthers) {
		this.task = task;
		this.notifyOthers = notifyOthers;
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
		Object result = null;
		try {
			result = task.execute(dependencyResults);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
		
		if (notifyOthers) {
			setChanged();
			notifyObservers(result);
		}
		return result;
		
	}
	
}
