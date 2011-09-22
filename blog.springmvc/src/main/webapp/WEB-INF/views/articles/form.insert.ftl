<html>
	<head>
		<title>Create Article</title>
		<link rel='stylesheet' type='text/css' href='[@spring.url value="/static/style.css"/]'>
	</head>
	<body>
		<h1>Create Article</h1>
	
		[@spring.url value='/articles.html' var='url'/]
		
		[@springf.form action=url method='post' modelAttribute='article']
			[#include '/views/articles/form.include.ftl'/]
			
			<input type='submit' value='Save'>
		[/@springf.form]
	</body>
</html>