package github.familysyan.concurrent.tasks.orchestrator;

public class OrchestratorFactory {
	
	private static final ThreadLocal<Orchestrator> store = new ThreadLocal<Orchestrator>();
	
	/**
	 * Get the orchestrator for the current thread.
	 * @return Orchestrator
	 */
	public static Orchestrator getOrchestrator() {
		Orchestrator orchestrator = store.get();
		if (orchestrator == null) {
			orchestrator = new Orchestrator.Builder().build();
			store.set(orchestrator);
		}
		return orchestrator;
	}
	
	public static void setOrchestrator(Orchestrator orchestrator) {
		store.set(orchestrator);
	}

}
