package github.familysyan.concurrent.tasks.orchestrator;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import github.familysyan.concurrent.tasks.Task;
import github.familysyan.concurrent.tasks.TaskConfiguration;
import github.familysyan.concurrent.tasks.executor.TaskExecutor;

public class Orchestrator {

	private TaskExecutor taskExecutor;
	private TaskManager taskManager;
	
	private Orchestrator(Builder builder) {
		this.taskExecutor = new TaskExecutor(builder.executorService);
		this.taskManager = new TaskManager(taskExecutor, builder.shutdownWhenIdle);
	}
	
	public static class Builder {
		
		private boolean shutdownWhenIdle;
		private ExecutorService executorService;
		
		public Builder() {
			this.executorService = Executors.newCachedThreadPool();
		}
		
		/**
		 * @param executorService The customized ExecutorService
		 */
		public Builder(ExecutorService executorService) {
			this.executorService = executorService;
		}

		/**
		 * Automatically shutdown the orchestrator when all submitted tasks are
		 * finished. </br>
		 * Mainly useful for long tasks.</br>
		 * Use with caution when tasks are short.
		 * 
		 * @param shutdownWhenIdle
		 */
		public Builder setShutdownWhenIdle(boolean shutdownWhenIdle) {
			this.shutdownWhenIdle = shutdownWhenIdle;
			return this;
		}
		
		public Orchestrator build() {
			return new Orchestrator(this);
		}
	}

	/**
	 * Submit the task to this orchestrator. The task will be scheduled to be executed as soon as possible.
	 * @param task The task to be executed.
	 */
	public void acceptTask(Task<?> task) {
		acceptTask(task, null);
	}


	/**
	 * A blocking method to get the result of the certain task.
	 * @param uniqueTaskId The task id
	 * @param timeout The time to wait
	 * @param timeUnit The time unit
	 * @return The execution result of this task
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	public Object getTaskResult(String uniqueTaskId, long timeout, TimeUnit timeUnit)
			throws InterruptedException, ExecutionException, TimeoutException {
		Future<?> future = taskManager.getTaskResultFuture(uniqueTaskId);
		return future.get(timeout, timeUnit);
	}
	
	public Object getTaskResult(String uniqueTaskId)
			throws InterruptedException, ExecutionException, TimeoutException {
		Future<?> future = taskManager.getTaskResultFuture(uniqueTaskId);
		return future.get();
	}

	/**
	 * Submit the task to the orchestrator. The task will be automatically
	 * executed when all of its dependencies are finished.
	 * 
	 * @param task
	 *            The task to be executed
	 * @param tc
	 *            The configuration of the submitted task.
	 */
	public void acceptTask(Task<?> task, TaskConfiguration tc) {
		if (task != null) {
			taskManager.submitTask(task, tc);
		}
	}

	/**
	 * Manually shutdown the orchestrator when it's no longer needed.
	 */
	public void shutdown() {
		taskExecutor.shutdown();
	}

}
