package github.familysyan.concurrent.tasks.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import github.familysyan.concurrent.tasks.exceptions.ExecutorDownException;

public class TaskExecutor {

	private ExecutorService executor;

	public TaskExecutor() {
		this.executor = Executors.newCachedThreadPool();
	}

	/**
	 * @param executor
	 *            customized ExecutorService. if null, a cached thread pool will
	 *            be used as default executor.
	 */
	public TaskExecutor(ExecutorService executor) {
		if (executor == null) {
			this.executor = Executors.newCachedThreadPool();
		} else {
			this.executor = executor;
		}
	}

	/**
	 * Execute future task. Will throw runtime exception if the executor has already been shutdown.
	 * @param futureTask the future task to be executed
	 */
	public void executeTask(FutureTask<?> futureTask) {
		if (futureTask != null) {
			if (executor == null || executor.isShutdown()) {
				throw new ExecutorDownException();
			} else {
				executor.execute(futureTask);
			}
		}
	}

	/**
	 * Shut down the executor
	 */
	public void shutdown() {
		executor.shutdown();
	}

}
