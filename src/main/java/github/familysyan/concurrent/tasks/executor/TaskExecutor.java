package github.familysyan.concurrent.tasks.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import github.familysyan.concurrent.tasks.exceptions.ExecutorDownException;

public class TaskExecutor {
	
	private ExecutorService executor;
	
	public TaskExecutor() {
		
	}
	
	public TaskExecutor(ExecutorService executor) {
		if (executor == null) {
			this.executor = Executors.newCachedThreadPool();
		} else {
			this.executor = executor;
		}
	}

	public void executeTask(FutureTask<?> futureTask) {
		if (futureTask != null) {
			if (executor == null || executor.isShutdown()) {
				throw new ExecutorDownException();
			} else {
				executor.execute(futureTask);
			}
		}
	}
	
	public void shutdown() {
		executor.shutdown();
	}

}
