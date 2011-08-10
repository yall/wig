package helpers;

import java.util.ArrayList;
import java.util.List;

public class Command {

	static class Builder {
		
		public Builder args(String... args) {
			return new Builder();
		}
		
		public List<String> execute() {
			return new ArrayList<String>();
		}
		
	}
	
	public static Builder Builder() {
		return new Builder();
	}
	
}
