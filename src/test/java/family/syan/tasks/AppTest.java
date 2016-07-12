package family.syan.tasks;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import github.familysyan.concurrent.tasks.TaskConfiguration;
import github.familysyan.concurrent.tasks.orchestrator.Orchestrator;
import github.familysyan.concurrent.tasks.orchestrator.OrchestratorFactory;

/**
 * Unit test for simple App.
 */
public class AppTest {
	
	@Test
	public void testNormalCase() {
		Orchestrator orchestrator = new Orchestrator.Builder().build();
		OrchestratorFactory.setOrchestrator(orchestrator);
		GenerateWordsTask task1 = new GenerateWordsTask();
		orchestrator.acceptTask(task1);
		WordCountTask task2 = new WordCountTask();
		TaskConfiguration tc = new TaskConfiguration(task2).addDependency(task1);
		orchestrator.acceptTask(task2, tc);
		try {
			int count = (Integer) orchestrator.getTaskResult(task2.getUniqueTaskId(), 1000, TimeUnit.MILLISECONDS);
			assertTrue(count == 2);
//			orchestrator.shutdown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testLongTask() {
		Orchestrator orchestrator = new Orchestrator.Builder().build();
		OrchestratorFactory.setOrchestrator(orchestrator);
		LongTask task1 = new LongTask();
		orchestrator.acceptTask(task1);
		WordCountTask task2 = new WordCountTask();
		TaskConfiguration tc = new TaskConfiguration(task2).addDependency(task1);
		orchestrator.acceptTask(task2, tc);
		try {
			int count = (Integer) orchestrator.getTaskResult(task2.getUniqueTaskId());
			assertEquals(count, 5);
//			orchestrator.shutdown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMultipleDependency() {
		Orchestrator orchestrator = new Orchestrator.Builder().build();
		OrchestratorFactory.setOrchestrator(orchestrator);
		LongTask task1 = new LongTask();
		orchestrator.acceptTask(task1);
		GenerateWordsTask task2 = new GenerateWordsTask();
		orchestrator.acceptTask(task2);
		WordCountTask task3 = new WordCountTask();
		TaskConfiguration tc = new TaskConfiguration(task3).addDependency(task1).addDependency(task2);
		orchestrator.acceptTask(task3, tc);
		try {
			int count = (Integer) orchestrator.getTaskResult(task3.getUniqueTaskId());
			assertEquals(count, 7);
//			orchestrator.shutdown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testTimeout() {
		Orchestrator orchestrator = new Orchestrator.Builder().build();
		OrchestratorFactory.setOrchestrator(orchestrator);
		TimeoutTask task1 = new TimeoutTask();
		orchestrator.acceptTask(task1);
		WordCountTask task2 = new WordCountTask();
		TaskConfiguration tc = new TaskConfiguration(task2).addDependency(task1);
		orchestrator.acceptTask(task2, tc);
		try {
			int count = (Integer) orchestrator.getTaskResult(task2.getUniqueTaskId(), 200, TimeUnit.MILLISECONDS);
			assertEquals(0, count);
//			orchestrator.shutdown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
