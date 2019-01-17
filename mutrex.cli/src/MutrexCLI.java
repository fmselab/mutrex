import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import dk.brics.automaton.OORegexConverter;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import regex.distinguishing.DistinguishingString;
import regex.mutrex.ds.DSSet;
import regex.mutrex.ds.RegExpSet;
import regex.mutrex.main.GeneratorType;
import regex.mutrex.main.GeneratorType.Orientation;
import regex.mutrex.main.MutRex;
import regex.operators.Char2MetaChar;

@Command(name = "mutrex", mixinStandardHelpOptions = true, version = "mutrex cli 1.0")

public class MutrexCLI implements Runnable {
    @Option(names = { "-v", "--verbose" }, description = "Verbose mode. Helpful for troubleshooting. ")
    private boolean verbose;

	
	@Parameters(arity="1", paramLabel="regex", description="starting regex (us eof quotes maybe necessary)")
    String regex;
	
    public void run() {
    	//
    	if (!verbose) {
    		Logger.getLogger(OORegexConverter.class.getName()).setLevel(Level.OFF);
    		Logger.getLogger(Char2MetaChar.class.getName()).setLevel(Level.OFF);
    	}
    	//
    	System.out.println("tests for regular expression " + regex);
    	DSSet x = MutRex.generateStrings(regex, GeneratorType.BASIC, Orientation.RANDOM);
		Iterator<DistinguishingString> it = x.iterator();
		while(it.hasNext()) {
			DistinguishingString ds = it.next();
			RegExpSet value = x.getKilledMutants(ds);
			assert !value.isEmpty();
			System.out.println(ds + " kills " + value);
		}

    	
    }
    
    public static void main(String[] args) {
        CommandLine.run(new MutrexCLI(), args);
    }
}