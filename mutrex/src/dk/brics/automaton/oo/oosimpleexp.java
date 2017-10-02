package dk.brics.automaton.oo;

/* simpleexp	::=	charexp --> REGEX_CHAR		
	|	.	(any single character)	--> REGEXP_SPECIALCHAR
	|	#	(the empty language)	[OPTIONAL] --> REGEXP_SPECIALCHAR
	|	@	(any string)	[OPTIONAL] --> REGEXP_SPECIALCHAR
	|	" <Unicode string without double-quotes> "	(a string)	
	|	( )	(the empty string)	
	|	( unionexp )	(precedence override)	
// IN OTHER CLASSES
	|	< <identifier> >	(named automaton)	[OPTIONAL]
	|	<n-m>	(numerical interval)	[OPTIONAL]*/

// represents a STRING that matches with the STRING
public class oosimpleexp extends ooregex {
	public String s;

	public oosimpleexp(String s) {
		/* troppo forti, potrei avere una stringa che è solo un punto, basta che usi \.
		assert ! s.equals(".");
		assert ! s.equals("#");
		assert ! s.equals("@");
		assert ! s.contains(".") : 
		assert ! s.contains("#");
		assert ! s.contains("@");*/
		this.s = s;
		assert s.length() > 0: "not valid empty string"; // or > 1???
	}

	@Override
	public <T> T accept(RegexVisitor<T> v) {
		return v.visit(this);
	}
}
