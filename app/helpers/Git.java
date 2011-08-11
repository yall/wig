package helpers;

import java.io.File;
import java.util.List;

public class Git {

	private File repositoryDir;
	
	private Git(String repositoryPath) {
		this.repositoryDir = new File(repositoryPath);
	}
	
	public static Git repo() {
		return new Git(Wig.STORAGE_DIR);
	}
	
	public void add(String file) {
		new Command.Builder()
			.args("git", "add")
			.args(file)
			.execute(this.repositoryDir);
	}
	
	public void commit(String message) {
		new Command.Builder()
			.args("git", "commit", "-m")
			.args('"' + message + '"')
			.execute(this.repositoryDir);
	}
	
	public List<String> log(String file) {
		return
			new Command.Builder()
				.args("git", "log", "--format=format:%H\t%at\t%cn\t%s")
				.args(file)
				.execute(this.repositoryDir);
	}
	
	public void username(String username) {
		new Command.Builder()
			.args("git", "config", "user.name")
			.args("\"" + username + "\"")
			.execute(this.repositoryDir);
	}
}
