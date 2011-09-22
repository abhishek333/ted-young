<html>
	<head>
		<title>Register</title>
		<link rel='stylesheet' type='text/css' href='[@spring.url value="/static/style.css"/]'>
	</head>
	<body>
		<h1>Register: Step 2 - Contact</h1>
	
		[@spring.url value='/authors/register/contact.html' var='url'/]
		
		[@springf.form action=url method='put' modelAttribute='registration']
			<dl>
				<dt><label>First Name:</label> ${registration.firstName?html}</dt>
			
				<dt><label>Last Name:</label>
					[@spring.bind path='lastName']${status.value}[/@spring.bind]
				</dt>
			
				<dt><label>Email:</label></dt>
				<dd>
					[@springf.errors path='email'/]
					[@springf.input path='email'/]
				</dd>
				
				<dt><label>Phone Number:</label></dt>
				<dd>
					[@springf.errors path='phoneNumber'/]
					[@springf.input path='phoneNumber'/]
				</dd>
			</dl>
			<input type='submit' value='Next'>
		[/@springf.form]
		
		<a href="[@spring.url value='/authors/register/name.html'/]">Back</a>
	</body>
</html>