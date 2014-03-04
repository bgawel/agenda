<g:if env="development">
<!DOCTYPE html>
<html>
	<head>
		<title>Grails Runtime Exception</title>
		<meta name="layout" content="main">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'errors.css')}" type="text/css">
	</head>
	<body>
    <g:renderException exception="${exception}" />
	</body>
</html>
</g:if>
<g:else>
  <g:message code="error.unexpected" locale="${new Locale('pl')}"/>
</g:else>