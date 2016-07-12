package github.familysyan.concurrent.tasks.internal;

import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import github.familysyan.concurrent.tasks.Task;
import github.familysyan.concurrent.tasks.orchestrator.ExecutorFactory;

@SuppressWarnings("rawtypes")
public class TaskTimeSensitiveWrapper extends InternalTask{

	private TaskWrapper taskWrapper;
	
	public TaskTimeSensitiveWrapper(Task<?> task) {
		this.taskWrapper = new TaskWrapper(task, false);
	}
	
	public Task getTask() {
		return taskWrapper.getTask();
	}
	
	/**
	 * Returns true if this task is ready to be executed. Otherwise returns false.
	 * @return true/false
	 */
	public boolean isReady() {
		return taskWrapper.isReady();
	}
	
	/**
	 * Add dependency of this task.
	 * @param dependencyId The id of the dependency task.
	 */
	public void addDependency(String dependencyId) {
		taskWrapper.addDependency(dependencyId);
	}
	
	/**
	 * This method will be called when some dependency has successfully finished execution.
	 * @param dependencyId The id of the finished dependency.
	 * @param dependencyResult The execution result of the finished dependency.
	 */
	public void fulfillDependency(String dependencyId, Object dependencyResult) {
		taskWrapper.fulfillDependency(dependencyId, dependencyResult);
	}

	@SuppressWarnings("unchecked")
	public Object call() throws Exception {
		FutureTask futureTask = new FutureTask(taskWrapper);
		Executor executor = ExecutorFactory.getExecutor();
		executor.execute(futureTask);
		Object result = null;
		try {
			long timeout = taskWrapper.getTask().getTimeout();
			result = futureTask.get(timeout, TimeUnit.MILLISECONDS);
		} catch (TimeoutException e) {
			futureTask.cancel(true);
			taskWrapper.getTask().failedToComplete();
			throw new Exception(e);
		} finally {
			setChanged();
			notifyObservers(result);
			
		}
		return result;
		
	}
}
