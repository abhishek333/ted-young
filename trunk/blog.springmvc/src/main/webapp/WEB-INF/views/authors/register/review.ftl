<html>
	<head>
		<title>Register</title>
		<link rel='stylesheet' type='text/css' href='[@spring.url value="/static/style.css"/]'>
	</head>
	<body>
		<h1>Register: Step 3 - Review</h1>
	
		
		<dl>
			<dt><label>First Name:</label> ${registration.firstName?html}</dt>
		
			<dt><label>Last Name:</label> ${registration.lastName?html}</dt>
		
			<dt><label>Email:</label> ${registration.email?html}</dt>
			
			<dt><label>Phone Number:</label> ${registration.phoneNumber}</dt>
		</dl>
		
		<a href="[@spring.url value='/authors/register/contact.html'/]">Back</a>
	</body>
</html>