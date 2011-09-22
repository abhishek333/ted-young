<html>
	<head>
		<title>Register</title>
		<link rel='stylesheet' type='text/css' href='[@spring.url value="/static/style.css"/]'>
	</head>
	<body>
		<h1>Register: Step 1 - Name</h1>
	
		[@spring.url value='/authors/register/name.html' var='url'/]
		
		[@springf.form action=url method='put' modelAttribute='registration']
			<dl>
				<dt><label>First Name:</label></dt>
				<dd>
					[@springf.errors path='firstName'/]
					[@springf.input path='firstName'/]
				</dd>
				
				<dt><label>Last Name:</label></dt>
				<dd>
					[@springf.errors path='lastName'/]
					[@springf.input path='lastName'/]
				</dd>
			</dl>
			<input type='submit' value='Next'>
		[/@springf.form]
	</body>
</html>