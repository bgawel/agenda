<%@ page contentType="text/html" %>
<html>
    <head>
        <title><g:message code='confirmation.title' locale="${locale}" /></title>
    </head>        
    <body>
        <h4>
            <g:message code='confirmation.resetpwd.text' locale="${locale}" />
        </h4>
        <g:set var="redirectTo" value="${baseUri + uri[uri.lastIndexOf('/')..-1] + '/' + username}" />
        <a href="${redirectTo}">${redirectTo}</a>
        <p>
          <small><g:message code='confirmation.text.help' locale="${locale}" /></small>
        </p>
        <p>
          <small><g:message code='confirmation.warn' locale="${locale}" /></small>
        </p>
    </body>
</html>  