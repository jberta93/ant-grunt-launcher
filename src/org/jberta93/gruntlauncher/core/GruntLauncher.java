package org.jberta93.gruntlauncher.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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
	private String npmCommand = null;


	@Override
	public void execute() {
		if (getGruntfileDir() == null) {
			throw new BuildException("No gruntfileDir set.");
		}
		long time = System.currentTimeMillis();

		log("[GruntLauncher] Starting execution");
		log("  Directory : " + getGruntfileDir());
		if (getExecuteNpmInstall() != null && getExecuteNpmInstall()) {
			log("  Step 1    : npm install");
		}
		if (getExecuteBowerInstall() != null && getExecuteBowerInstall()) {
			log("  Step 2    : bower install");
		}
		if (getNpmCommand() != null) {
			log("  Command   : npm " + getNpmCommand());
		} else {
			log("  Command   : grunt " + getGruntTask());
		}
		if (getEnviormentPath() != null) {
			log("  PATH+     : " + getEnviormentPath());
		}
		log("-----------------------------");

		String cwd = getGruntfileDir();
		File execFile = null;

		try {
			execFile = BashFileManager.createShellScript(getGruntfileDir(), getGruntTask(), getEnviormentPath(), getExecuteNpmInstall(),
					getExecuteBowerInstall(), getNpmCommand());
		} catch (IOException e) {
			throw new BuildException("Failed to create shell script: " + e.getMessage(), e);
		}

		if (execFile != null) {
			int exitCode;
			if (OSUtils.getOS().equals(OperatingSystem.WINDOWS)) {
				exitCode = executeCommand(cwd, "cmd.exe", "/c", execFile.getName());
			} else if (OSUtils.getOS().equals(OperatingSystem.OSX) || OSUtils.getOS().equals(OperatingSystem.LINUX)) {
				exitCode = executeCommand(cwd, "./" + execFile.getName());
			} else {
				throw new BuildException("Unknown operating system: " + OSUtils.getOS());
			}

			if (exitCode != 0) {
				throw new BuildException("Task failed with exit code: " + exitCode);
			}
		}

		execFile.delete();

		String operation = getNpmCommand() != null ? "npm " + getNpmCommand() : "grunt " + getGruntTask();
		log("[GruntLauncher] " + operation + " completed in " + (System.currentTimeMillis() - time) + " ms");
	}

	private int executeCommand(String cwd, String... commands) {

		Process p;
		try {
			ProcessBuilder pb = new ProcessBuilder(commands);
			// Map<String, String> environment = pb.environment();

			pb.directory(new File(cwd));
			pb.redirectErrorStream(true);
			p = pb.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				log(line);
			}
			int exitCode = p.waitFor();

			return exitCode;

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

	public String getNpmCommand() {
		return this.npmCommand;
	}

	public void setNpmCommand(String npmCommand) {
		this.npmCommand = npmCommand;
	}
}
