package github.familysyan.concurrent.tasks.example;

import java.util.List;

import github.familysyan.concurrent.tasks.Task;

public class GenerateWordsTask implements Task<String>{

	public String getUniqueTaskId() {
		return "word_generator";
	}

	public String execute(List<Object> dependencies) {
		return "Hello world";
	}

}
