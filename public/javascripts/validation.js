angular.module('JSONSchemaValidationApp', ['ui.ace'])
  .controller('MyController', ['$http', function($http) {

  var vm = this;
  vm.foo = "qq";
  vm.schema = JSON.stringify({
    properties: {
      foo: { type: "string" }
    }
  });
  vm.instance = "{}";
  vm.submit = function() {
    $http({
      method: 'POST',
      url: '/validate',
      data: $.param({schema: this.schema, instance: this.instance}),
      headers: {'Content-Type': 'application/x-www-form-urlencoded'}
      }).then(
      function(result) { console.log('success ' + JSON.stringify(result)); }, function() { console.log('error'); }
    );
    console.log("LOLBERT");
  }
}]);