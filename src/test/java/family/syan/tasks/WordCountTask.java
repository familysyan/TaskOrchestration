package family.syan.tasks;

import java.util.List;
import java.util.StringTokenizer;

import github.familysyan.concurrent.tasks.Task;

public class WordCountTask implements Task<Integer>{


	public String getUniqueTaskId() {
		return "word_counter";
	}

	public Integer execute(List<Object> dependencies) {
		if (dependencies != null && dependencies.size() > 0) {
			int count = 0;
			for (Object dependency : dependencies) {
				if (dependency != null) {
					String words = (String) dependency;
					StringTokenizer t = new StringTokenizer(words);
					count = count + t.countTokens();
				}
			}
			return count;
		} else {
			return -1;
		}
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
