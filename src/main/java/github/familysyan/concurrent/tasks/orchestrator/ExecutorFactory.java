package github.familysyan.concurrent.tasks.orchestrator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorFactory {
	
private static final ThreadLocal<ExecutorService> store = new ThreadLocal<ExecutorService>();
	
	public static ExecutorService getExecutor() {
		ExecutorService executor = store.get();
		if (executor == null) {
			executor = Executors.newCachedThreadPool();
			store.set(executor);
		}
		return executor;
	}
	
	protected static void setExecutor(ExecutorService executor) {
		store.set(executor);
	}

}
