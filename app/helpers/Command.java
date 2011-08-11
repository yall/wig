package helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Command {

	private List<String> args;
	
	public Command(Builder builder) {
		this.args = new ArrayList<String>(builder.args);
	}
	
	public List<String> execute(File directory) {
        try {
            Process process = new ProcessBuilder()
                    .command(args)
                    .directory(directory)
                    .redirectErrorStream(true)
                    .start();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            List<String> outputLines = new ArrayList<String>();
            String outputLine;
            while ((outputLine = in.readLine()) != null) {
                outputLines.add(outputLine);
            }

            if (process.waitFor() != 0) {
                StringBuilder message = new StringBuilder();
                for (String line : outputLines) {
                    message.append("\n").append(line);
                }
                throw new RuntimeException("Process failed: " + args + message);
            }

            return outputLines;
        } catch (IOException e) {
            throw new RuntimeException("Process failed: " + args, e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Process failed: " + args, e);
        }
	}
	
	static class Builder {
		
		private List<String> args = 
			new ArrayList<String>();
		
		public Builder args(String... args) {
			this.args.addAll(Arrays.asList(args));
			return this;
		}
		
		public List<String> execute(File directory) {
			return build().execute(directory);
		}
		
		public Command build() {
			return new Command(this);
		}
		
	}
	

	
}
