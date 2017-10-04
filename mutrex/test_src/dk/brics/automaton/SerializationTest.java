package dk.brics.automaton;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

public class SerializationTest {

	@Test
	public void test() throws IOException, ClassNotFoundException {
		RegExp rgx = new RegExp("[a-z]*");
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("rgx.dat"));
		out.writeObject(rgx);
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("rgx.dat"));
		RegExp newRgx = (RegExp) in.readObject();
		System.out.println(rgx);
		System.out.println(newRgx);
	}
}