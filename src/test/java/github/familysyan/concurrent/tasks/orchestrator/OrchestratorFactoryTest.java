package github.familysyan.concurrent.tasks.orchestrator;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.junit.Test;

import junit.framework.TestCase;

public class OrchestratorFactoryTest extends TestCase {
	
	@Test
	public void test1() {
		assertNotNull(OrchestratorFactory.getOrchestrator());
	}
	
	@Test
	public void test2() {
		Orchestrator o1 = OrchestratorFactory.getOrchestrator();
		Orchestrator o2 = OrchestratorFactory.getOrchestrator();
		assertTrue(o1 == o2);
	}
	
	public void test3() throws InterruptedException, ExecutionException {
		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<Orchestrator> callable = new Callable<Orchestrator>() {

			@Override
			public Orchestrator call() throws Exception {
				return OrchestratorFactory.getOrchestrator();
			}
			
		};
		FutureTask<Orchestrator> task1 = new FutureTask<Orchestrator>(callable);
		FutureTask<Orchestrator> task2 = new FutureTask<Orchestrator>(callable);
		executor.execute(task1);
		executor.execute(task2);
		Orchestrator o1 = task1.get();
		Orchestrator o2 = task2.get();
		assertTrue(o1 != o2);
	}

}
