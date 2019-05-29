package regex.oracle;

import java.util.Scanner;

public class UserOracle implements RegexOracle {

	@Override
	public boolean accept(String s) {
		return askUser(s, "accept");
	}

	private boolean askUser(String s, String acceptReject) {
		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.println("Do you " + acceptReject + " \"" + s + "\"?" + " Y for yes, N for no");
			String line = in.nextLine();
			if (line.equalsIgnoreCase("Y")) {
				return true;
			}
			if (line.equalsIgnoreCase("N")) {
				return false;
			}
			System.out.println("I did not understand.");
		}
	}
}