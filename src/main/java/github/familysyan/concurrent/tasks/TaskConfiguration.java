package github.familysyan.concurrent.tasks;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class TaskConfiguration {
	
	private List<Task> dependencies = new ArrayList<Task>();
	private Task task;
	
	public TaskConfiguration(Task task) {
		this.task = task;
	}

	public TaskConfiguration addDependency(Task dependency) {
		if (dependency != null) {
			dependencies.add(dependency);
		}
		return this;
	}
	
	public Task getTask() {
		return task;
	}
	
	public List<Task> getDependencies() {
		return dependencies;
	}

}
