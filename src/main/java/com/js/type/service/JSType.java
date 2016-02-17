package com.js.type.service;

import com.js.type.concurrency.Result;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import tern.EcmaVersion;
import tern.ITernProject;
import tern.TernException;
import tern.TernResourcesManager;
import tern.resources.TernProject;
import tern.server.ITernDef;
import tern.server.ITernPlugin;
import tern.server.TernDef;
import tern.server.TernPlugin;
import tern.server.nodejs.NodejsTernServer;
import tern.server.nodejs.process.NodejsProcessManager;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class JSType implements Serializable{

	private static final boolean SERVER_NO_PORT_FILE = true;
	private static final boolean SERVER_VERBOSE_FLAG = false;
	private static final boolean SERVER_PERSISTENT_MODE_FLAG = true;
	private static final long SERVER_TIMEOUT = 5000L;
	private static final String TERN_DIR = "./ternjs/node_modules/tern";

	public List<String> getTypes(JSTypes jsType, String zipFilePath) throws IOException, InterruptedException, TernException, ExecutionException {
		List<String> jsFiles = getJsFilesToQuery(zipFilePath);
		File nodejsBaseDir = getTernRepositoryDir();

		solveDependencies(jsType, zipFilePath);
		TernProject project = (TernProject) createProject(zipFilePath, jsFiles);
		initProject(nodejsBaseDir, project);
		NodejsTernServer server = createTernServer(project);

		List<String> results = new Result().getResults(jsFiles, server);

		return results;
	}

	private List<String> getJsFilesToQuery(String zipFilePath) {
		List<String> files = new ArrayList<>();
		Collection<File> jsFiles = FileUtils.listFiles(
				new File(zipFilePath),
				new org.apache.commons.io.filefilter.RegexFileFilter(".*(js)"),
				DirectoryFileFilter.DIRECTORY
		);

		for (File jsFile : jsFiles) {
			files.add(jsFile.getAbsolutePath());
		}
		return files;
	}

	private void solveDependencies(JSTypes jsType, String queryBaseDir) throws IOException, InterruptedException {
		if(jsType == JSTypes.NODEJS){
			ProcessBuilder processBuilder = new ProcessBuilder("npm", "install");
			processBuilder.directory(new File(queryBaseDir));
			Process p = processBuilder.start();
			p.waitFor();
		}
	}

	private void initProject(File nodejsBaseDir, ITernProject project) {
		NodejsProcessManager.getInstance().init(nodejsBaseDir);
		((tern.resources.TernProject)project).add("node_timeout", 5000L);
	}

	private NodejsTernServer createTernServer(ITernProject project) throws TernException {
		NodejsTernServer server = new NodejsTernServer(project);
		server.setVerbose(SERVER_VERBOSE_FLAG);
		server.setTimeout(SERVER_TIMEOUT);
		server.setPersistent(SERVER_PERSISTENT_MODE_FLAG);
		server.setNoPortFile(SERVER_NO_PORT_FILE);
		return server;
	}
	
	private ITernProject createProject(String ternProjectLocation, List<String> queryFiles) throws IOException {
		TernProject project = (TernProject) TernResourcesManager.getTernProject(new File(ternProjectLocation));
		project.setEcmaVersion(EcmaVersion.ES6);
		
		ITernDef browserDef = TernDef.browser;
		ITernDef jqueryDef = TernDef.jquery;
		
		project.addLib(browserDef);
		project.addLib(jqueryDef);
		
		ITernPlugin nodePlugin = TernPlugin.node;
		ITernPlugin requirejsPlugin = TernPlugin.requirejs;
		ITernPlugin outlinePlugin = TernPlugin.outline;
		
		project.addPlugin(nodePlugin);
		project.addPlugin(requirejsPlugin);
		project.addPlugin(outlinePlugin);

		for (String file : queryFiles) {
			project.addLoadEagerlyPattern(file);
		}
		project.save();
		return project;
	}
	
	private File getTernRepositoryDir() {
		return new File(TERN_DIR);
	}

}
