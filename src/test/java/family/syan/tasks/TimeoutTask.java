package family.syan.tasks;

import java.util.List;

import github.familysyan.concurrent.tasks.Task;

public class TimeoutTask implements Task<Object>{

	@Override
	public String getUniqueTaskId() {
		return "Long_run_task";
	}

	@Override
	public Object execute(List<Object> dependencies) {
		try {
		    Thread.sleep(1000);                 
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		return "this is a long task";
	}

	@Override
	public void failedToComplete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getTimeout() {
		return 100;
	}

}
