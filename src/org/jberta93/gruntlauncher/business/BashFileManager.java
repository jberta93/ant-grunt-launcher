package org.jberta93.gruntlauncher.business;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BashFileManager {

	private static final String FILENAME = "gruntexectmp";

	/**
	 * Check current OS and create the proper script to execute
	 *
	 * @param dir directory where script file is created
	 * @param gruntTask task name registered in Gruntfile.js
	 * @param enviromentVariableToConcat enviroment variables to add in the PATH for grunt / node
	 * @param executeNpmInstall if script must execute npm install command
	 * @param executeBowerInstall if script must execute bower install command
	 * @return script to exec
	 * @throws IOException
	 */
	public static File createShellScript(String dir, String gruntTask, String enviromentVariableToConcat, Boolean executeNpmInstall,
			Boolean executeBowerInstall) throws IOException {
		String osName = System.getProperty("os.name").toLowerCase();
		File execFile = null;
		if (osName.indexOf("win") >= 0) {
			execFile = createBatFile(dir, gruntTask, enviromentVariableToConcat, executeNpmInstall, executeBowerInstall);
		} else {
			execFile = createShFile(dir, gruntTask, enviromentVariableToConcat, executeNpmInstall, executeBowerInstall);
		}

		return execFile;
	}

	/**
	 * Create sh script to execute grunt command in UNIX OS and give execution permission to file
	 *
	 * @param dir directory where script file is created
	 * @param gruntTask task name registered in Gruntfile.js
	 * @param enviromentVariableToConcat enviroment variables to add in the PATH for grunt / node
	 * @param executeNpmInstall if script must execute npm install command
	 * @param executeBowerInstall if script must execute bower install command
	 * @return script to exec
	 * @throws IOException
	 */
	private static File createShFile(String dir, String gruntTask, String enviromentVariableToConcat, Boolean executeNpmInstall,
			Boolean executeBowerInstall) throws IOException {

		File f = new File(dir + "/" + FILENAME + ".sh");

		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);

		StringBuilder sb = new StringBuilder();
		sb.append("#/bin/bash \n");

		if (enviromentVariableToConcat != null) {
			sb.append("export PATH=");
			sb.append(enviromentVariableToConcat);
			sb.append(":$PATH \n");
		}

		if (executeNpmInstall != null && executeNpmInstall) {

			sb.append("echo \"npm install started\" \n");
			sb.append("npm install \n");
			sb.append("echo \"npm install ended\" \n");
		}

		if (executeBowerInstall != null && executeBowerInstall) {
			sb.append("echo \"bower install started\" \n");
			sb.append("bower install \n");
			sb.append("echo \"bower install ended\" \n");
		}

		sb.append("grunt ");
		sb.append(gruntTask);
		sb.append(" \n");

		bw.write(sb.toString());
		bw.flush();
		bw.close();

		f.setExecutable(true, false);
		f.setReadable(true, false);
		f.setWritable(true, false);

		return f;
	}

	/**
	 * Create bat script to execute grunt command in Microsoft Windows and give execution permission to file
	 *
	 * @param dir directory where script file is created
	 * @param gruntTask task name registered in Gruntfile.js
	 * @param enviromentVariableToConcat enviroment variables to add in the PATH for grunt / node
	 * @param executeNpmInstall if script must execute npm install command
	 * @param executeBowerInstall if script must execute bower install command
	 * @return script to exec
	 * @throws IOException
	 */

	private static File createBatFile(String dir, String gruntTask, String enviromentVariableToConcat, Boolean executeNpmInstall,
			Boolean executeBowerInstall) throws IOException {

		File f = new File(dir + "/" + FILENAME + ".bat");

		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);

		StringBuilder sb = new StringBuilder();

		if (enviromentVariableToConcat != null) {
			sb.append("set PATH=");
			sb.append("%PATH%;");
			sb.append(enviromentVariableToConcat);
			sb.append("\n");
		}

		if (executeNpmInstall != null && executeNpmInstall) {
			sb.append("echo \"npm install started\" \n");
			sb.append("npm install \n");
			sb.append("echo \"npm install ended\" \n");
		}

		if (executeBowerInstall != null && executeBowerInstall) {
			sb.append("echo \"bower install started\" \n");
			sb.append("bower install \n");
			sb.append("echo \"bower install ended\" \n");
		}

		sb.append("grunt ");
		sb.append(gruntTask);
		sb.append(" \n");

		sb.append("exit 0");

		bw.write(sb.toString());
		bw.flush();
		bw.close();

		f.setExecutable(true, false);
		f.setReadable(true, false);
		f.setWritable(true, false);

		return f;
	}

}
