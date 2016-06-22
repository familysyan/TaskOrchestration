package github.familysyan.concurrent.tasks.example;

import java.util.List;
import java.util.StringTokenizer;

import github.familysyan.concurrent.tasks.Task;

public class WordCountTask implements Task<Integer>{


	public String getUniqueTaskId() {
		return "word_counter";
	}

	public Integer execute(List<Object> dependencies) {
		if (dependencies != null && dependencies.size() > 0) {
			String words = (String) dependencies.get(0);
			StringTokenizer t = new StringTokenizer(words);
			return t.countTokens();
		} else {
			return -1;
		}
	}

}
