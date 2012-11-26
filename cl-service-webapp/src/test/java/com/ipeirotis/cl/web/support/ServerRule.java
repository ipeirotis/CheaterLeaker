package com.ipeirotis.cl.web.support;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.junit.rules.ExternalResource;

import winstone.Launcher;

public class ServerRule extends ExternalResource {
	private Launcher launcher;

	@Override
	protected void before() throws Throwable {
		Launcher.initLogger(new HashMap<Object, Object>());
		
		Map<String, Object> args = new HashMap<String, Object>();
		
		args.put("webroot", "src/main/webapp");
		
		launcher = new Launcher(args);
		
	}
	
	@Override
	protected void after() {
		launcher.shutdown();
	}
	
	public static void main(String[] args) throws Throwable {
		ServerRule serverRule = new ServerRule();
		
		serverRule.before();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		reader.readLine();
		
		serverRule.after();
	}
}
