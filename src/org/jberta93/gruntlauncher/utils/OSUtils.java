package org.jberta93.gruntlauncher.utils;

import org.jberta93.gruntlauncher.enums.OperatingSystem;

public class OSUtils {

	private static String OS = System.getProperty("os.name").toLowerCase();

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}

	public static boolean isSolaris() {
		return (OS.indexOf("sunos") >= 0);
	}

	public static OperatingSystem getOS() {
		if (isWindows()) {
			return OperatingSystem.WINDOWS;
		} else if (isMac()) {
			return OperatingSystem.OSX;
		} else if (isUnix()) {
			return OperatingSystem.LINUX;
		} else if (isSolaris()) {
			return OperatingSystem.SOLARIS;
		} else {
			return OperatingSystem.UNKNOWN;
		}
	}
}
