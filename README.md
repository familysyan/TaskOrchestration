TaskOrchestration
=============

This project is to help manage the execution of runnable tasks. A task can have dependency on other tasks and it will be executed only when all of its dependencies have been finished.

A DAG graph is used to manage the dependency of tasks. The DAG graph used in this project is [JGrapht](http://jgrapht.org/)

### Example
#### Create a task
```Java
public class GenerateWordsTask implements Task<String>{

  	public String getUniqueTaskId() {
  		return "word_generator";
  	}

  	public String execute(List<Object> dependencies) {
  		return "Hello world";
  	}

}
```
#### Create another task
```Java
public class WordCountTask implements Task<Integer>{


	public String getUniqueTaskId() {
		return "word_counter";
	}

	public Integer execute(List<Object> dependencies) {
		if (dependencies != null && dependencies.size() > 0) {
			int count = 0;
			for (Object dependency : dependencies) {
				String words = (String) dependency;
				StringTokenizer t = new StringTokenizer(words);
				count = count + t.countTokens();
			}
			return count;
		} else {
			return -1;
		}
	}

}
```
#### Start orchestrating
```Java
Orchestrator orchestrator = new Orchestrator.Builder().setShutdownWhenIdle(false).build(); // Create an orchestrator
OrchestratorFactory.setOrchestrator(orchestrator); // Make the orchestrator shareable 
GenerateWordsTask task1 = new GenerateWordsTask(); // Create a task
orchestrator.acceptTask(task1); // submit the task
WordCountTask task2 = new WordCountTask(); // Create another task
TaskConfiguration tc = new TaskConfiguration(task2).addDependency(task1); // Make task2 depend on task1
orchestrator.acceptTask(task2, tc); // Submit task2
try {
	int count = (Integer) orchestrator.getTaskResult(task2.getUniqueTaskId(), 1000, TimeUnit.MILLISECONDS); // Get the result of task2
	assertTrue(count == 2);
	orchestrator.shutdown(); // Shutdown the orchestrator manually
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
```

### Contribute to this project
Pull requests are welcome! Please also include junit tests in your pull request.
