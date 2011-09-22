<html>
	<head>
		<title>View Article</title>
		<link rel='stylesheet' type='text/css' href='[@spring.url value="/static/style.css"/]'>
	</head>
	<body>
		<h1>View Article</h1>
		
		[@messages/]
		[@image/]
		
		<dl>
			<dt>
				<label>Title:</label> 
				${article.title?html}
			</dt>
			
			<dt>
				<label>Content:</label> 
			</dt>
			<dd>
				${article.content?html}
			</dd>
		</dl>
		
		<p>
			<a href="[@spring.url value='/articles/${article.id}/form.html'/]">
				Edit
			</a>
		</p>
		
		<form action="[@spring.url value='/articles/${article.id}.html'/]" method="POST">
			<input type='hidden' name='_method' value='delete'>
			<input type='submit' value='Delete'>
		</form>
		
		<p>
			<a href="[@spring.url value='/articles.html'/]">
				Return To List
			</a>
		</p>
	</body>
</html>

[#macro messages]
	[#if message??]
		[#if message = "inserted"]<h2>Article Saved.</h2>[/#if]
	[/#if]
[/#macro]

[#macro image]
	<div class='aside'>
		[#if article.imagePresent]
			<div>
				<img src='[@spring.url value="/articles/${article.id}.jpg"/]'>
			</div>
		[/#if]
		
		<form action="[@spring.url value='/articles/${article.id}/image.html'/]" enctype='multipart/form-data' method="POST">
			Upload Image: <input type='file' name='image'>
			<input type='submit' value='Upload'>
		</form>
	</div>
[/#macro]