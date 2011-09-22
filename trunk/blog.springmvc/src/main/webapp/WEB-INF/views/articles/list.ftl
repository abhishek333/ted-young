<html>
	<head>
		<title>Article List</title>
		<link rel='stylesheet' type='text/css' href='[@spring.url value="/static/style.css"/]'>
	</head>
	<body>
		<h1>
			[@spring.message code="title.welcome"/]
		</h1>
	
		<table>
			<thead>
				<tr>
					<th></th>
					<th>Title</th>
				</tr>
			</thead>
			<tbody>
				[#list articles as article]
					<tr>
						<td>
							<a href="[@spring.url value='/articles/${article.id}.html'/]">
								Open
							</a>
						</td>
						<td>
							${article.title?html}
						</td>
					</tr>
				[/#list]
			</tbody>
		</table>
		
		<form action="[@spring.url value='/articles.html'/]" method='GET'>
			<p>
				Filter: <input type='text' name='query' value='${query!""}'> <input type='submit' value='Search'>
			</p>
		</form>
		
		<p>
			<a href="[@spring.url value='/articles/form.html'/]">
				New
			</a>
		</p>
	</body>
</html>