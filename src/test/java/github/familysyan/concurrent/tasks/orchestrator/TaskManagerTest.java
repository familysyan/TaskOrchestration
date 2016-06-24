package github.familysyan.concurrent.tasks.orchestrator;

import java.util.List;

import org.junit.Test;

import github.familysyan.concurrent.tasks.Task;
import github.familysyan.concurrent.tasks.TaskConfiguration;
import github.familysyan.concurrent.tasks.exceptions.RedundantTaskException;
import github.familysyan.concurrent.tasks.exceptions.TaskNotFoundException;
import github.familysyan.concurrent.tasks.executor.TaskExecutor;

public class TaskManagerTest{
	
	private TaskExecutor taskExecutor = new TaskExecutor();
	private Task<Integer> task1 = new Task<Integer>() {

		@Override
		public String getUniqueTaskId() {
			return "task1";
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Integer execute(List dependencies) {
			return 1;
		}	
	};
	
	private Task<Integer> task2 = new Task<Integer>() {

		@Override
		public String getUniqueTaskId() {
			return "task2";
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Integer execute(List dependencies) {
			return 2;
		}	
	};
	
	@Test(expected = RedundantTaskException.class)
	public void testRedundantTaskExecption() {
		TaskManager manager = new TaskManager(taskExecutor, false);
		
		manager.submitTask(task1, null);
		manager.submitTask(task1, null);
	}
	
	@Test(expected = TaskNotFoundException.class)
	public void testTaskNotFoundExecption() {
		TaskManager manager = new TaskManager(taskExecutor, false);
		TaskConfiguration tc = new TaskConfiguration(task1).addDependency(task2);
		manager.submitTask(task1, tc);
	}
	

}
