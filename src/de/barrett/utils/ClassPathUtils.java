package de.barrett.utils;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassPathUtils {
	
	// PUBLIC -------------------------------------------------------- //
	
	public static void loadClassPaths(String[] classPaths) throws Exception {
		for (String classPath : classPaths) {
			loadClassPath(Utils.createUrlQuietly(classPath));
		}
	}
	
	public static void loadClassPath(URL u) throws Exception {
		URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Class<URLClassLoader> sysclass = URLClassLoader.class;
		Method method = sysclass.getDeclaredMethod("addURL", new Class[] { URL.class });
		method.setAccessible(true);
		method.invoke(sysloader, new Object[] { u });
	}
	
	@SuppressWarnings("unchecked")
	public static <C> C loadClass(String name) throws Exception {
		return (C) ClassLoader.getSystemClassLoader().loadClass(name).newInstance();
	}
	
}