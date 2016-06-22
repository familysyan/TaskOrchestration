package github.familysyan.concurrent.tasks.orchestrator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;
import org.jgrapht.graph.DefaultEdge;

import github.familysyan.concurrent.tasks.Task;
import github.familysyan.concurrent.tasks.TaskConfiguration;
import github.familysyan.concurrent.tasks.exceptions.DeadlockException;
import github.familysyan.concurrent.tasks.exceptions.RedundantTaskException;
import github.familysyan.concurrent.tasks.exceptions.TaskNotFoundException;
import github.familysyan.concurrent.tasks.executor.TaskExecutor;
import github.familysyan.concurrent.tasks.internal.TaskWrapper;

public class TaskManager implements Observer{
	private Map<String, FutureTask<?>> results = new HashMap<String, FutureTask<?>>();
	private Map<String, TaskWrapper> internalTasks = new HashMap<String, TaskWrapper>();
	private Set<String> pendingTasks = new HashSet<String>();
	private TaskExecutor taskExecutor;
	private DirectedAcyclicGraph<String, DefaultEdge> graph = new DirectedAcyclicGraph<String, DefaultEdge>(DefaultEdge.class);
	private boolean shutdownExecutorWhenIdle;
	
	protected TaskManager(TaskExecutor taskExecutor, boolean shutdownExecutorWhenIdle) {
		this.taskExecutor = taskExecutor;
		this.shutdownExecutorWhenIdle = shutdownExecutorWhenIdle;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void submitTask(Task<?> task, TaskConfiguration tc) {
		if (task == null) {
			return;
		}
		if (internalTasks.get(task.getUniqueTaskId()) != null) {
			throw new RedundantTaskException(task.getUniqueTaskId());
		}
		pendingTasks.add(task.getUniqueTaskId());
		TaskWrapper internalTask = new TaskWrapper(task);
		internalTask.addObserver(this);
		graph.addVertex(task.getUniqueTaskId());
		if (tc != null) {
			for (Task<?> dependency : tc.getDependencies()) {
				TaskWrapper internalNode = internalTasks.get(dependency.getUniqueTaskId());
				if (internalNode == null) {
					throw new TaskNotFoundException(dependency.getUniqueTaskId());
				} else {
					internalTask.addDependency(dependency.getUniqueTaskId());
					if (!graph.containsEdge(task.getUniqueTaskId(), dependency.getUniqueTaskId())) {
						try {
							graph.addDagEdge(task.getUniqueTaskId(), dependency.getUniqueTaskId());
						} catch (CycleFoundException e) {
							throw new DeadlockException();
						}
					} 
					
				}
			}
		}
		internalTasks.put(task.getUniqueTaskId(), internalTask);
		FutureTask<?> futureTask = new FutureTask(internalTask);
		results.put(task.getUniqueTaskId(), futureTask);
		Set<DefaultEdge> outgoingEdges = graph.outgoingEdgesOf(task.getUniqueTaskId());
		for (DefaultEdge edge : outgoingEdges) {
			String target = graph.getEdgeTarget(edge);
			FutureTask<?> targetResult = results.get(target);
			if (targetResult.isDone()) {
				try {
					internalTask.dependencyReady(target, targetResult.get());
				} catch (InterruptedException e) {

				} catch (ExecutionException e) {
					
				}
			}
		}
		if (internalTask.isReady()) {
			taskExecutor.executeTask(futureTask);
		}
	}

	protected FutureTask<?> getTaskResultFuture(String taskId) {
		return results.get(taskId);
	}

	public void update(Observable o, Object arg) {
		if (o instanceof TaskWrapper) {
			TaskWrapper finishedTask = (TaskWrapper) o;
			Set<DefaultEdge> incomingEdges = graph.incomingEdgesOf(finishedTask.getTask().getUniqueTaskId());
			for (DefaultEdge edge : incomingEdges) {
				String source = graph.getEdgeSource(edge);
				TaskWrapper task = internalTasks.get(source);
				task.dependencyReady(finishedTask.getTask().getUniqueTaskId(), arg);
				if (task.isReady()) {
					taskExecutor.executeTask(results.get(task.getTask().getUniqueTaskId()));
				}
			}
			pendingTasks.remove(finishedTask.getTask().getUniqueTaskId());
			if (shutdownExecutorWhenIdle) {
				if (pendingTasks.isEmpty()) {
					taskExecutor.shutdown();
				}
			}
			
		}
	}
}
