package github.familysyan.concurrent.tasks.internal;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.junit.Test;

import github.familysyan.concurrent.tasks.Task;
import junit.framework.TestCase;

public class TaskWrapperTest extends TestCase {
	
	Task<Integer> t = new Task<Integer>() {

		@Override
		public String getUniqueTaskId() {
			return "testTask";
		}

		@Override
		public Integer execute(List<Object> dependencies) {
			return 1;
		}
		
	};
	
	@Test
	public void testGetTask() {
		TaskWrapper tWrapper = new TaskWrapper(t);
		assertTrue(tWrapper.getTask() == t);
	}
	
	@Test
	public void test2() {
		TaskWrapper tWrapper = new TaskWrapper(t);
		assertTrue(tWrapper.getTask().getUniqueTaskId().equals("testTask"));
	}
	
	@Test
	public void testIsReady() {
		TaskWrapper tWrapper = new TaskWrapper(t);
		assertTrue(tWrapper.isReady());
		tWrapper.addDependency("some other task");
		assertFalse(tWrapper.isReady());
	}
	
	@Test
	public void testIsReady2() {
		TaskWrapper tWrapper = new TaskWrapper(t);
		tWrapper.addDependency("some other task");
		assertFalse(tWrapper.isReady());
		tWrapper.fulfillDependency("some other task", 0);
		assertTrue(tWrapper.isReady());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testNotifyGetCalled() throws InterruptedException, ExecutionException {
		TaskWrapper tWrapper = new TaskWrapper(t);
		ObserverForTest observer = new ObserverForTest();
		tWrapper.addObserver(observer);
		ExecutorService executor = Executors.newCachedThreadPool();
		FutureTask<Integer> task = new FutureTask<Integer>(tWrapper);
		executor.execute(task);
		assertTrue(1 == task.get());
		assertTrue("TaskWrapper did not call \"notify()\"", 1 == observer.state);
	}
	
	private static class ObserverForTest implements Observer {
		
		private int state = -1;

		@Override
		public void update(Observable o, Object arg) {
			state = 1;
			
		}
		
	}

}
