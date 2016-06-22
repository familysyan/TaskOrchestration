package github.familysyan.concurrent.tasks.orchestrator;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import github.familysyan.concurrent.tasks.Task;
import github.familysyan.concurrent.tasks.TaskConfiguration;
import github.familysyan.concurrent.tasks.executor.TaskExecutor;

public class Orchestrator {
	
	private TaskExecutor taskExecutor;
	private TaskManager taskManager;
	private boolean initialized = false;
	private ExecutorService executorService;
	private boolean shutdownExecutorWhenIdle = true;
	
	public void initialize() {
		if (!initialized) {
			taskExecutor = new TaskExecutor(executorService);
			taskManager = new TaskManager(taskExecutor, shutdownExecutorWhenIdle);
			initialized = true;
		}
	}
	
	public Orchestrator setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
		return this;
	}
	
	public Orchestrator shutdownWhenIdle(boolean shutdownWhenIdle) {
		this.shutdownExecutorWhenIdle = shutdownWhenIdle;
		return this;
	}

	public void acceptTask(Task<?> task) {
		acceptTask(task, null);
	}

	public Object getTaskResult(String uniqueTaskId, int timeoutInMilliseconds) throws InterruptedException, ExecutionException, TimeoutException {
		Future<?> future = taskManager.getTaskResultFuture(uniqueTaskId);
		return future.get();
	}

	public void acceptTask(Task<?> task, TaskConfiguration tc) {
		if (task != null) {
			taskManager.submitTask(task, tc);
		}
	}
	
	public void shutdown() {
		taskExecutor.shutdown();
	}
	
}
