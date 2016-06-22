package github.familysyan.concurrent.tasks.example;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import github.familysyan.concurrent.tasks.TaskConfiguration;
import github.familysyan.concurrent.tasks.orchestrator.Orchestrator;
import github.familysyan.concurrent.tasks.orchestrator.OrchestratorFactory;

public class Example {

	public static void main(String[] args) {
		Orchestrator orchestrator = OrchestratorFactory.getOrchestrator();
		orchestrator.shutdownWhenIdle(false).initialize();
		GenerateWordsTask task1 = new GenerateWordsTask();
		orchestrator.acceptTask(task1);
		WordCountTask task2 = new WordCountTask();
		TaskConfiguration tc = new TaskConfiguration(task2).addDependency(task1);
		orchestrator.acceptTask(task2, tc);
		orchestrator.shutdown();
		
		try {
			int count = (Integer) orchestrator.getTaskResult(task2.getUniqueTaskId(), 1000);
			System.out.println(count);
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
