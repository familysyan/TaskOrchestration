package github.familysyan.concurrent.tasks.orchestrator;

public class OrchestratorFactory {
	
	private static final ThreadLocal<Orchestrator> store = new ThreadLocal<Orchestrator>();
	
	public static Orchestrator getOrchestrator() {
		Orchestrator orchestrator = store.get();
		if (orchestrator == null) {
			orchestrator = new Orchestrator();
			store.set(orchestrator);
		}
		return orchestrator;
	}

}
