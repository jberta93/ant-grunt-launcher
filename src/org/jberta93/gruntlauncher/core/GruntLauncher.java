package org.jberta93.gruntlauncher.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.jberta93.gruntlauncher.business.BashFileManager;
import org.jberta93.gruntlauncher.enums.OperatingSystem;
import org.jberta93.gruntlauncher.utils.OSUtils;

public class GruntLauncher extends Task {

	private final String DEFAULT_GRUNT_TASK = "build";

	private String gruntTask = null;
	private String gruntfileDir = null;
	private String enviormentPath = null;
	private Boolean executeNpmInstall = null;
	private Boolean executeBowerInstall = null;

	@Override
	public void execute() {
		if (getGruntfileDir() == null) {
			throw new BuildException("No gruntfileDir set.");
		}
		long time = System.currentTimeMillis();

		log("Executing Grunt Task");
		log("Gruntfile.js Directory: " + getGruntfileDir());
		log("Grunt Task: " + getGruntTask());
		log("Enviroment Variables to add to PATH: " + getEnviormentPath());
		log("Execute NPM Install: " + (getExecuteNpmInstall() != null && getExecuteNpmInstall()));
		log("Execute Bower Install: " + (getExecuteBowerInstall() != null && getExecuteBowerInstall()));

		log("-----------------------------");
		String command = "";
		String cwd = getGruntfileDir();

		File execFile = null;

		try {
			execFile = BashFileManager.createShellScript(getGruntfileDir(), getGruntTask(), getEnviormentPath(), getExecuteNpmInstall(),
					getExecuteBowerInstall());
		} catch (IOException e) {
			throw new BuildException("No gruntfileDir set.");
		}

		if (execFile != null) {
			if (OSUtils.getOS().equals(OperatingSystem.WINDOWS)) {
				command = "run " + execFile.getName();
			} else if (OSUtils.getOS().equals(OperatingSystem.OSX) || OSUtils.getOS().equals(OperatingSystem.LINUX)) {
				command = "./" + execFile.getName();
			} else {
				throw new BuildException("Unknown Operating System.");
			}

			executeCommand(command, cwd);
		}

		execFile.delete();

		log("Grunt " + getGruntTask() + " ended in " + (System.currentTimeMillis() - time) + " ms");
	}

	private void executeCommand(String command, String cwd) {

		Process p;
		try {
			ProcessBuilder pb = new ProcessBuilder(command);
			Map<String, String> environment = pb.environment();

			pb.directory(new File(cwd));
			pb.redirectErrorStream(true);
			p = pb.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				log(line);
			}
			p.waitFor();

		} catch (Exception e) {
			throw new BuildException(e);
		}

	}

	/**
	 * Grunt task name to execute defined in Gruntfile.js
	 */
	public String getGruntTask() {
		return gruntTask != null ? gruntTask : DEFAULT_GRUNT_TASK;
	}

	public void setGruntTask(String gruntTask) {
		this.gruntTask = gruntTask;
	}

	/**
	 * Directory where Gruntfile.js is located
	 */
	public String getGruntfileDir() {
		return gruntfileDir;
	}

	public void setGruntfileDir(String gruntfileDir) {
		this.gruntfileDir = gruntfileDir;
	}

	/**
	 * Enviroment variables to add in the PATH for grunt / node
	 */
	public String getEnviormentPath() {
		return enviormentPath;
	}

	public void setEnviormentPath(String enviormentPath) {
		this.enviormentPath = enviormentPath;
	}

	/**
	 * If you want to execute npm install before grunt
	 */
	public Boolean getExecuteNpmInstall() {
		return executeNpmInstall;
	}

	public void setExecuteNpmInstall(Boolean executeNpmInstall) {
		this.executeNpmInstall = executeNpmInstall;
	}

	/**
	 * If you want to execute bower install before grunt and after npm install if active
	 */
	public Boolean getExecuteBowerInstall() {
		return executeBowerInstall;
	}

	public void setExecuteBowerInstall(Boolean executeBowerInstall) {
		this.executeBowerInstall = executeBowerInstall;
	}
}
