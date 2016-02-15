package com.js.type.service;

import com.js.type.util.FileUtil;
import com.js.type.util.UnzipUtil;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import tern.TernException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class JSTypeTest {
	JSType jsType;
	FileUtil fileUtil;
	UnzipUtil unzipUtil;
	
	@Before
	public void init(){
		jsType = new JSType();
		fileUtil = new FileUtil();
		unzipUtil = new UnzipUtil();
	}

	@Test
	public void testGetTypesNodeBrow() throws IOException, InterruptedException, TernException, JSONException, ExecutionException {
		String QUERY_BASE_DIR = "/home/sujithk/workspace/node-browserify";
		String QUERY_FILE = "index.js";

		List<String> actual = jsType.getTypes(JSTypes.NODEJS, QUERY_BASE_DIR);
//		String expected = fileUtil.getFile("node-browserify_index_types.json");
//		JSONAssert.assertEquals(expected, actual,false);
	}
	
	@Test
	public void testGetTypesRepo() throws IOException, InterruptedException, TernException, JSONException, ExecutionException {
		String QUERY_BASE_DIR = unzipUtil.unzip("/home/sujithk/js-repos-part/repo~joeyism~node-zeroes~42971394~false~JavaScript~master~1.zip");
		String QUERY_FILE = "index.js";

		List<String> actual = jsType.getTypes(JSTypes.NODEJS, QUERY_BASE_DIR);
//		String expected = fileUtil.getFile("repo_index_types.json");
//		JSONAssert.assertEquals(expected, actual,false);
	}

	@Test
	public void test() throws IOException {
		String QUERY_BASE_DIR = unzipUtil.unzip("/home/sujithk/js-repos-part/repo~28msec~28.io-angularjs~16548195~false~JavaScript~master~3.zip");
		System.out.println(QUERY_BASE_DIR);
	}
}
