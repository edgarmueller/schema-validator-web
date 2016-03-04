angular.module('JSONSchemaValidationApp', ['ui.ace'])
  .controller('SubmitValidationController', ['$http', function($http) {
  var vm = this;
  vm.schema = "{ \"minimum\": 1 }";
  vm.instance = "3";
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
          console.log(JSON.stringify(result));
          vm.isValid = vm.equalsInstance(result.data);
          vm.isInvalid = !vm.isValid;
          vm.errors = JSON.stringify(result.data, null, 4);
        },
        function(error) {
          vm.isInvalid = true;
          vm.errors = error;
        }
    );
  }
  vm.equalsInstance = function(a) {
    try {
     return JSON.stringify(JSON.parse(vm.instance)) === JSON.stringify(a);
    } catch (error) {
      return false;
    }
  }
  vm.resetValidationState = function() {
    if (!vm.isInvalid && !vm.isValid) {
      return;
    }
    vm.isInvalid = false;
    vm.isValid = false;
  }
}]);
