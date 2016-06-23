package github.familysyan.concurrent.tasks.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.junit.Test;

import github.familysyan.concurrent.tasks.exceptions.ExecutorDownException;
import junit.framework.TestCase;

public class TaskExecutorTest extends TestCase {
	
	
	Callable<Integer> callable = new Callable<Integer>() {

		@Override
		public Integer call() throws Exception {
			return 1;
		}
		
	};
	
	@Test
	public void test1() throws InterruptedException, ExecutionException {
		TaskExecutor executor = new TaskExecutor();
		FutureTask<Integer> task = new FutureTask<Integer>(callable);
		executor.executeTask(task);
		assertTrue("Default constructor of TaskExecutor is not working", task.get() == 1);
	}
	
	@Test
	public void test2() throws InterruptedException, ExecutionException {
		TaskExecutor executor = new TaskExecutor();
		executor.shutdown();
		FutureTask<Integer> task = new FutureTask<Integer>(callable);
		try {
			executor.executeTask(task);
		} catch(RuntimeException e) {
			assertEquals(ExecutorDownException.class, e.getClass());
		}
	}
	
	@Test
	public void test3() throws InterruptedException, ExecutionException {
		ExecutorService executor = Executors.newFixedThreadPool(1);
		TaskExecutor tExecutor = new TaskExecutor(executor);
		FutureTask<Integer> task = new FutureTask<Integer>(callable);
		tExecutor.executeTask(task);
		assertTrue(task.get() == 1);
	}

}
