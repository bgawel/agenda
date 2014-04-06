<!doctype html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Kulturalna Agenda Wrocławia</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width">
    
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">

    <link rel="stylesheet" href="${resource(dir: 'ng/css', file: 'main.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'ng/css', file: 'ngFonts.css')}" type="text/css">
</head>
  <body ng-app="frontendApp">
    <!--[if lt IE 7]>
      <p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
    <![endif]-->

    <!--[if lt IE 9]>
      <script src="${resource(dir: 'ng/js', file: 'es5-shim.min.js')}"></script>
      <script src="${resource(dir: 'ng/js', file: 'json3.min.js')}"></script>
    <![endif]-->

    <!-- Add your site or application content here -->
    <div ng-view=""></div>

    <!-- Google Analytics: change UA-XXXXX-X to be your site's ID -->
     <script>
       (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
       (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
       m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
       })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

       ga('create', 'UA-XXXXX-X');
       ga('send', 'pageview');
    </script>

    <script src="${resource(dir: 'ng/js', file: 'jquery.min.js')}"></script>
    <script src="${resource(dir: 'ng/js', file: 'angular.min.js')}"></script>
    <script src="${resource(dir: 'ng/js', file: 'plugins.js')}"></script>
    <script src="${resource(dir: 'ng/js', file: 'modules.js')}"></script>
    <script src="${resource(dir: 'ng/js', file: 'ui-templates.js')}"></script>
    <script src="${resource(dir: 'ng/js', file: 'scripts.js')}"></script>
        
</body>
</html>