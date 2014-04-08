angular.module('ui-templates', ['template/confirmationdialog/delete.html', 'template/confirmationdialog/info.html', 'template/confirmationdialog/question.html', 'template/infoPanel/infoPanel.html', 'template/loadingbar/loadingbar.html', 'template/datepicker/datepicker.html', 'template/datepicker/popup.html', 'template/popover/popover.html', 'template/timepicker/timepicker.html']);

angular.module("template/confirmationdialog/delete.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/confirmationdialog/delete.html",
    "<div class=\"modal-header\">\n" +
    "  <h6><span class=\"glyphicon glyphicon-trash danger-text\"></span> {{headerText}}</h6>\n" +
    "</div>\n" +
    "<div class=\"modal-body\">\n" +
    "  <h5>{{bodyText}}</h5>\n" +
    "</div>\n" +
    "<div class=\"modal-footer\">\n" +
    "  <button class=\"btn btn-danger\" ng-click=\"ok()\">Usuń</button>\n" +
    "  <button class=\"btn btn-warning\" ng-click=\"cancel()\" type=\"button\" autofocus>Anuluj</button>\n" +
    "</div>");
}]);

angular.module("template/confirmationdialog/info.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/confirmationdialog/info.html",
    "<div class=\"modal-header\">\n" +
    "  <h6><span class=\"glyphicon glyphicon-ok info-text\"></span> {{headerText}}</h6>\n" +
    "</div>\n" +
    "<div class=\"modal-body\">\n" +
    "  <h5>{{bodyText}}</h5>\n" +
    "</div>\n" +
    "<div class=\"modal-footer\">\n" +
    "  <button class=\"btn btn-info\" ng-click=\"ok()\" type=\"button\" autofocus>Ok</button>\n" +
    "</div>			");
}]);

angular.module("template/confirmationdialog/question.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/confirmationdialog/question.html",
    "<div class=\"modal-header\">\n" +
    "  <h6><span class=\"glyphicon glyphicon-question-sign question-text\"></span> {{headerText}}</h6>\n" +
    "</div>\n" +
    "<div class=\"modal-body\">\n" +
    "  <h5>{{bodyText}}</h5>\n" +
    "</div>\n" +
    "<div class=\"modal-footer\">\n" +
    "  <button class=\"btn btn-info\" ng-click=\"ok()\">Tak</button>\n" +
    "  <button class=\"btn btn-warning\" ng-click=\"cancel()\" type=\"button\" autofocus>Nie</button>\n" +
    "</div>");
}]);

angular.module("template/infoPanel/infoPanel.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/infoPanel/infoPanel.html",
    "<alert type=\"panel.type\" ng-show=\"panel.show\" ng-switch on=\"panel.messages.length\">\n" +
    "  <span ng-switch-when=\"1\">\n" +
    "    <span ng-show=\"panel.type === 'danger'\" class=\"glyphicon glyphicon-exclamation-sign error\"></span> {{panel.messages[0]}}\n" +
    "  </span>\n" +
    "  <ul ng-switch-default>\n" +
    "    <li ng-repeat=\"msg in panel.messages\">\n" +
    "      <span ng-show=\"panel.type === 'danger'\" class=\"glyphicon glyphicon-exclamation-sign error\"></span> {{msg}}\n" +
    "    </li>\n" +
    "  </ul>\n" +
    "</alert>\n" +
    "");
}]);

angular.module("template/loadingbar/loadingbar.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/loadingbar/loadingbar.html",
    "<progressbar class=\"progress-striped active\" value=\"75\" type=\"'info'\"></progressbar>");
}]);

angular.module("template/datepicker/datepicker.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/datepicker/datepicker.html",
    "<table>\n" +
    " <thead>\n" +
    "   <tr class=\"text-center\">\n" +
    "     <th><button type=\"button\" class=\"btn btn-default btn-sm pull-left\" ng-click=\"move(-1)\"><span class=\"glyphicon glyphicon-chevron-left\"></span></button></th>\n" +
    "     <th colspan=\"{{rows[0].length - 2 + showWeekNumbers}}\"><button type=\"button\" class=\"btn btn-block btn-default btn-sm\" ng-click=\"toggleMode()\"><strong>{{title}}</strong></button></th>\n" +
    "     <th><button type=\"button\" class=\"btn btn-default btn-sm pull-right\" ng-click=\"move(1)\"><span class=\"glyphicon glyphicon-chevron-right\"></span></button></th>\n" +
    "   </tr>\n" +
    "   <tr class=\"h6\" ng-show=\"labels.length > 0\">\n" +
    "     <th class=\"text-center\" ng-show=\"showWeekNumbers\">#</th>\n" +
    "     <th class=\"text-center\" ng-repeat=\"label in labels\">{{label}}</th>\n" +
    "   </tr>\n" +
    " </thead>\n" +
    " <tbody>\n" +
    "   <tr ng-repeat=\"row in rows\">\n" +
    "     <td ng-show=\"showWeekNumbers\" class=\"h6 text-center\"><em>{{ getWeekNumber(row) }}</em></td>\n" +
    "     <td ng-repeat=\"dt in row\" class=\"text-center\">\n" +
    "       <button type=\"button\" style=\"width:100%;\" class=\"btn btn-xs\" ng-class=\"{'btn-info': dt.selected}\" ng-click=\"select(dt.date)\" ng-disabled=\"dt.disabled\"><span ng-class=\"{muted: dt.secondary}\">{{dt.label}}</span></button>\n" +
    "     </td>\n" +
    "   </tr>\n" +
    " </tbody>\n" +
    "</table>");
}]);

angular.module("template/datepicker/popup.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/datepicker/popup.html",
    "<ul class=\"dropdown-menu\" ng-style=\"{display: (isOpen && 'block') || 'none', top: position.top+'px', left: position.left+'px'}\">\n" +
    "  <li ng-transclude></li>\n" +
    "  <li class=\"divider\"></li>\n" +
    "  <li style=\"padding: 9px;\">\n" +
    "    <span class=\"btn-group\">\n" +
    "      <button type=\"button\" class=\"btn btn-sm btn-info\" ng-click=\"today()\">{{currentText}}</button>\n" +
    "      <button type=\"button\" class=\"btn btn-sm btn-default\" ng-click=\"showWeeks = ! showWeeks\" ng-class=\"{active: showWeeks}\">{{toggleWeeksText}}</button>\n" +
    "    </span>\n" +
    "    <button type=\"button\" class=\"btn btn-sm btn-success pull-right\" ng-click=\"isOpen = false\">{{closeText}}</button>\n" +
    "  </li>\n" +
    "</ul>");
}]);

angular.module("template/popover/popover.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/popover/popover.html",
    "<div class=\"popover {{placement}}\" ng-class=\"{ in: isOpen(), fade: animation() }\">\n" +
    "  <div class=\"arrow\"></div>\n" +
    "  <div class=\"popover-inner\">\n" +
    "    <h3 class=\"popover-title\" ng-bind=\"title\" ng-show=\"title\"></h3>\n" +
    "    <div class=\"popover-content\" ng-bind-html=\"content\"></div>\n" +
    "  </div>\n" +
    "</div>");
}]);

angular.module("template/timepicker/timepicker.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/timepicker/timepicker.html",
    "<table class=\"form-inline\">\n" +
    "  <tr>\n" +
    "    <td ng-class=\"{'error': invalidHours}\">\n" +
    "      <input type=\"text\" ng-model=\"hours\" ng-change=\"updateHours()\" class=\"form-control\" ng-mousewheel=\"incrementHours()\" ng-readonly=\"readonlyInput\" maxlength=\"2\">\n" +
    "    </td>\n" +
    "    <td>:</td>\n" +
    "    <td ng-class=\"{'error': invalidMinutes}\">\n" +
    "      <input type=\"text\" ng-model=\"minutes\" ng-change=\"updateMinutes()\" class=\"form-control\" ng-mousewheel=\"incrementMinutes()\" ng-readonly=\"readonlyInput\" maxlength=\"2\">\n" +
    "    </td>    \n" +
    "  </tr>\n" +
    "</table>");
}]);
