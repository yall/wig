package helpers;

import java.util.List;

public class Git {

	public void add(String file) {
		Command.Builder()
			.args("git", "add")
			.args(file)
			.execute();
	}
	
	public void commit(String message) {
		Command.Builder()
			.args("git", "commit", "-m")
			.args(message)
			.execute();
	}
}
