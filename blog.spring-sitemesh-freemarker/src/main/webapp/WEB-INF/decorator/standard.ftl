<!DOCTYPE html>

[#assign decorator = JspTaglibs["http://www.opensymphony.com/sitemesh/decorator"]/]
[@decorator.usePage id="page"/]

<html>
	<head>
		<title>[@decorator.title/]</title>
		[@decorator.head/]
	</head>
	<body>
		[@decorator.body/]
		
		<div id="footer">Copyright &copy; Ted Young ${now?string("yyyy")}</div>
	</body>
</html>
		
 
