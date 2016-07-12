package family.syan.tasks;

import java.util.List;

import github.familysyan.concurrent.tasks.Task;

public class GenerateWordsTask implements Task<String>{

	public String getUniqueTaskId() {
		return "word_generator";
	}

	public String execute(List<Object> dependencies) {
		return "Hello world";
	}

	@Override
	public void failedToComplete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getTimeout() {
		// TODO Auto-generated method stub
		return 0;
	}

}
