<%@ page contentType="text/html" %>
<html>
    <head>
        <title><g:message code='confirmation.title' locale="${locale}" /></title>
    </head>        
    <body>
        <h4>
            <g:message code='confirmation.signup.text' locale="${locale}" />
        </h4>
        <a href="${uri}">${uri}</a>
        <g:if test="${inst}">
          <br>
          <g:message code='confirmation.signup.admin.name' locale="${locale}" />: ${inst.name}
          <br>
          <g:message code='confirmation.signup.admin.email' locale="${locale}" />: ${inst.email}
          <br>
          <g:message code='confirmation.signup.admin.address' locale="${locale}" />: ${inst.address}
          <br>
          <g:message code='confirmation.signup.admin.web' locale="${locale}" />: ${inst.web}
          <br>
          <g:message code='confirmation.signup.admin.telephone' locale="${locale}" />: ${inst.telephone}
          <br>
          <g:message code='confirmation.signup.admin.when' locale="${locale}" />: ${inst.dateCreated}
          <br>
        </g:if>
        <p>
          <small><g:message code='confirmation.text.help' locale="${locale}" /></small>
        </p>
        <p>
          <small><g:message code='confirmation.warn' locale="${locale}" /></small>
        </p>
    </body>
</html>  