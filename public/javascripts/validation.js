angular.module('JSONSchemaValidationApp', ['ui.ace'])
  .controller('SubmitValidationController', ['$http', function($http) {
  var vm = this;
  vm.schema = "{}";
  vm.instance = "{}";
  vm.isValid = false;
  vm.isInvalid = false;
  vm.submit = function() {
    $http({
      method: 'POST',
      url: '/validate',
      data: $.param({schema: vm.schema, instance: vm.instance}),
      headers: {'Content-Type': 'application/x-www-form-urlencoded'}
      }).then(
        function(result) {
          vm.isValid = Object.keys(result.data).length == 0;
          vm.isInvalid = !vm.isValid;
          vm.errors = JSON.stringify(result.data, null, 4);
        },
        function(error) {
          vm.errors = error;
        }
    );
  }
}]);