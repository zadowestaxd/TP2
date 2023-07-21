package simulator.launcher;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Test;

class MainTest {

	static private boolean run(String inFile, String outFile, String expOutFile, Integer ticks) {

		try {
			simulator.launcher.Main.main(new String[] { "-i", inFile, "-o", outFile, "-t", ticks.toString() });

			File currRunOutFile = new File(outFile);
			File expectedOutFile = new File(expOutFile);
			
			JSONObject jo1 = new JSONObject(new JSONTokener(new FileInputStream(currRunOutFile)));
			JSONObject jo2 = new JSONObject(new JSONTokener(new FileInputStream(expectedOutFile)));
			
			//currRunOutFile.delete();
			
			JSONObject jo1_aux = new JSONObject(jo1.toString());
			JSONObject jo2_aux = new JSONObject(jo2.toString());
			
			System.out.println(jo1_aux);
			System.out.println(jo2_aux); //este creo que es el que está mal, al final del string
			
			return jo1_aux.similar(jo2_aux);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	void test_1() {
		assertTrue(run("resources/examples/ex1.json", "resources/tmp/ex1.junit.out.json", "resources/examples/ex1.expout.json", 300));

	}

	@Test
	void test_2() {
		assertTrue(run("resources/examples/ex2.json", "resources/tmp/ex2.junit.out.json", "resources/examples/ex2.expout.json", 300));

	}

	@Test
	void test_3() {
		assertTrue(run("resources/examples/ex3.json", "resources/tmp/ex3.junit.out.json", "resources/examples/ex3.expout.json", 150));

	}

}
