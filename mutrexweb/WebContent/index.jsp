<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="regex.mutrex.*"%>
<%@ page import="regex.mutrex.ds.*"%>
<%@ page import="regex.operators.*"%>
<%@ page import="regex.distinguishing.*"%>
<%@ page import="regex.mutrex.main.*"%>
<%@ page import="dk.brics.automaton.*"%>
<%@ page import="dk.brics.automaton.oo.*"%>
<%@ page import="mutrexweb.ipapi.Ipapi"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Generate strings from regex</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style>
body {
	font-family: Arial, Helvetica Neue, Helvetica, sans-serif;
}

h1 {
	color: blue;
	background: white;
	margin-left: 0px;
	vertical-align: middle;
}

h2 {
	font-size: 120%;
}

table, th, td {
	border-collapse: collapse;
	border: 1px solid black;
}

th {
	height: 40px;
}

td {
	padding: 6px;
}
</style>
<script language="JavaScript">
	function toggle(source) {
		checkboxes = document.getElementsByName('mop');
		for (var i = 0, n = checkboxes.length; i < n; i++) {
			checkboxes[i].checked = source.checked;
		}
	}
</script>
</head>
<body>
	<%!public void printDs(javax.servlet.jsp.JspWriter out, DSSet ds, boolean confirming) throws java.io.IOException {
		for (DistinguishingString d : ds) {
			if (d.isConfirming() == confirming) {
				out.println("<tr>");
				out.println("<td>\"" + d.getDs() + "\"</td>");
				out.println("<td>");
				RegExpSet rs = ds.getKilledMutants(d);
				out.println("(size: " + rs.size() + ")");
				for (dk.brics.automaton.RegExp r : rs) {
					// print the description for each regex killed
					out.println(rs.getDescription(r).toString());
					out.println(ToSimpleString.convertToReadableString(OORegexConverter.getOORegex(r)));
				}
				out.println("</td></tr>");
			}
		}
	}%>
	<h1>
		<img src="icon/mutrex_small.png" alt="mutrex logo"
			style="width: 60px; height: 60px;">MutRex: the mutation-based
		test generator for regular expressions
	</h1>
	<%
		if (request.getParameter("regex") == null) {
	%>
	<form action="index.jsp">
		Reg exp: <input type="text" name="regex" size="45">
		<button type="submit" name="action" value="generate">Generate
			strings</button>
		<p>
			<!-- the algorithms -->
		<h2>MutRex algorithm:</h2>
		<INPUT TYPE="radio" NAME="option"
			VALUE="<%=GeneratorType.BASIC.name()%>" CHECKED>Basic
		algorithm<BR> <INPUT TYPE="radio" NAME="option"
			VALUE="<%=GeneratorType.MONITORING.name()%>">Use monitoring<BR>
		<INPUT TYPE="radio" NAME="option"
			VALUE="<%=GeneratorType.COLLECTING_NO_LIMIT.name()%>">Use
		collecting<BR>
		<!-- the mutations -->
		<h2>Mutation operators:</h2>
		<table>
			<tr>
				<td><input type="checkbox" onClick="toggle(this)" checked />
					Toggle All</td>
				<%
					int i = 2;
						for (RegexMutator m : AllMutators.definedMutators) {
				%>
				<td><input type="checkbox" name="mop" value="<%=m.getCode()%>"
					checked> <%=m.getCode()%> (<%=m.getClass().getSimpleName()%>)
				</td>
				<%
					if ((i % 3) == 0)
								out.print("</tr><tr>");
							i++;
						}
				%>
			</tr>
		</table>
		<h2>MutRex orientation:</h2>
		<INPUT TYPE="radio" NAME="pref"
			VALUE="<%=GeneratorType.Orientation.RANDOM.name()%>" CHECKED>RANDOM<BR>
		<INPUT TYPE="radio" NAME="pref"
			VALUE="<%=GeneratorType.Orientation.PREF_ACCEPT.name()%>">Prefer
		accepted<BR> <INPUT TYPE="radio" NAME="pref"
			VALUE="<%=GeneratorType.Orientation.PREF_REJECT.name()%>">Prefer
		rejected<BR>
	</form>
	<hr>
	<h2>MutRex syntax</h2>
	The syntax accepted by MutRex can be found here
	<a
		href="http://www.brics.dk/automaton/doc/dk/brics/automaton/RegExp.html">http://www.brics.dk/automaton/doc/dk/brics/automaton/RegExp.html</a>
	<br> MutRex supports also
	<br> \d is short for [0-9]
	<br> \w stands for "word character"
	<br> \s stands for "whitespace character"
	<br>
	<hr>
	Some examples can be downloaded
	<a href="mutation2017experiments.txt">here</a> and some more
	<a href="SImutation2017experiments.txt">here</a>
	<script>
		(function(i, s, o, g, r, a, m) {
			i['GoogleAnalyticsObject'] = r;
			i[r] = i[r] || function() {
				(i[r].q = i[r].q || []).push(arguments)
			}, i[r].l = 1 * new Date();
			a = s.createElement(o), m = s.getElementsByTagName(o)[0];
			a.async = 1;
			a.src = g;
			m.parentNode.insertBefore(a, m)
		})(window, document, 'script',
				'https://www.google-analytics.com/analytics.js', 'ga');

		ga('create', 'UA-93568019-1', 'auto');
		ga('send', 'pageview');
	</script>
	<%
		} else {
			// save the IP address and location
			String ip = request.getHeader("X-Forwarded-For");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			Ipapi.saveData(getServletContext().getRealPath("/"), request.getParameter("regex"), ip);
			// build the tests
			String option = request.getParameter("option");
			out.println("Strings for <b>" + request.getParameter("regex") + "</b>:<p	>\n");
			out.println("generated by " + option + "</b>:<p	>\n");
			//String options[] = request.getParameterValues("option");
			DSSet ds = MutRex.generateStrings(request.getParameter("regex"), option,
					request.getParameterValues("mop"), request.getParameter("pref"));
	%>
	<table>
		<thead>
			<tr>
				<th>String</th>
				<th>Killed mutants</th>
			</tr>
			<tr>
				<th>Accepted</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<%
				printDs(out, ds, true);
			%>
			<tr>
				<th>Rejected</th>
				<th></th>
			</tr>
			<%
				printDs(out, ds, false);
			%>
		</tbody>
	</table>
	<%
		}
	%>
	<hr>
	MutRex is still experimental - see papers:
	<br> P. Arcaini, A. Gargantini, E. Riccobene
	<br>
	<i>Fault-based test generation for regular expressions by mutation</i>
	<br> in Software Testing, Verification and Reliability, 2019
	<br>
	<a href="https://cs.unibg.it/gargantini/research/papers/mutrexSIstvr2017.pdf">[download the pdf]</a>, <a href="https://doi.org/10.1002/stvr.1664">[doi]</a>
	<br>
	<br>
	<br> P. Arcaini, A. Gargantini, E. Riccobene
	<br>
	<i>MutRex: a mutation-based generator of fault detecting strings for regular expressions</i>
	<br> in 12th International Workshop on Mutation Analysis (Mutation 2017), Tokyo, Japan, March 13, 2017
	<br>
	<a href="http://cs.unibg.it/gargantini/research/papers/mutrex_mutation17.pdf">[download the pdf]</a>, <a href="https://doi.org/10.1109/ICSTW.2017.23">[doi]</a>
	<hr>
	Source code is available
	<a href="https://github.com/fmselab/mutrex"> on github</a>
</body>
</html>
