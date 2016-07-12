package github.familysyan.concurrent.tasks.internal;

import java.util.Observable;
import java.util.concurrent.Callable;

import github.familysyan.concurrent.tasks.Task;

@SuppressWarnings("rawtypes")
public abstract class InternalTask extends Observable implements Callable{

	public abstract Task getTask();

	public abstract boolean isReady();

	public abstract void addDependency(String dependencyId);

	public abstract void fulfillDependency(String dependencyId, Object dependencyResult);
	
	
}
