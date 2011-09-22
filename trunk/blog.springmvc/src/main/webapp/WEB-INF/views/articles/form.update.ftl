<html>
	<head>
		<title>Edit Article</title>
		<link rel='stylesheet' type='text/css' href='[@spring.url value="/static/style.css"/]'>
	</head>
	<body>
		<h1>Edit Article</h1>
	
		[@spring.url value='/articles/${article.id}.html' var='url'/]
		
		[@springf.form action=url method='put' modelAttribute='article']
			[#include '/views/articles/form.include.ftl'/]
			
			<input type='submit' value='Save'>
		[/@springf.form]
	</body>
</html>