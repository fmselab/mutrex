
public class temp {

	
	public static void main(String[] args) {
		System.out.println(ddd("a"));
		System.out.println(ddd("\""));
		System.out.println(ddd("x\""));
		System.out.println(ddd("\\\""));
	}
	
	
	static String ddd(String s) {
		System.out.print("'" + s + "'" + "   -> ");
		return s.replaceAll("[^\\\\]\"", "\\\\\"");
	} 
}
