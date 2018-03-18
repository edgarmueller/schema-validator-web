var exampleSchema = {
                 "$schema": "http://json-schema.org/draft-04/schema#",
                 "title": "Product set",
                 "type": "array",
                 "items": {
                     "title": "Product",
                     "type": "object",
                     "properties": {
                         "id": {
                             "description": "The unique identifier for a product",
                             "type": "number"
                         },
                         "name": {
                             "type": "string"
                         },
                         "price": {
                             "type": "number",
                             "minimum": 0,
                             "exclusiveMinimum": true
                         },
                         "tags": {
                             "type": "array",
                             "items": {
                                 "type": "string"
                             },
                             "minItems": 1,
                             "uniqueItems": true
                         },
                         "dimensions": {
                             "type": "object",
                             "properties": {
                                 "length": {"type": "number"},
                                 "width": {"type": "number"},
                                 "height": {"type": "number"}
                             },
                             "required": ["length", "width", "height"]
                         },
                         "warehouseLocation": {
                             "description": "Coordinates of the warehouse with the product",
                             "$ref": "http://json-schema.org/geo"
                         }
                     },
                     "required": ["id", "name", "price"]
                 }
             };

var exampleInstance = [
                 {
                     "id": 2,
                     "name": "An ice sculpture",
                     "price": 12.50,
                     "tags": ["cold", "ice"],
                     "dimensions": {
                         "length": 7.0,
                         "width": 12.0,
                         "height": 9.5
                     },
                     "warehouseLocation": {
                         "latitude": -78.75,
                         "longitude": 20.4
                     }
                 },
                 {
                     "id": 3,
                     "name": "A blue mouse",
                     "price": 25.50,
                     "dimensions": {
                         "length": 3.1,
                         "width": 1.0,
                         "height": 1.0
                     },
                     "warehouseLocation": {
                         "latitude": 54.4,
                         "longitude": -32.7
                     }
                 }
             ]

angular.module('JSONSchemaValidationApp', ['ui.ace'])
  .controller('SubmitValidationController', ['$http', '$httpParamSerializer', function($http, $httpParamSerializer) {
  var vm = this;
  vm.schema = JSON.stringify(exampleSchema, null, 2);
  vm.instance = JSON.stringify(exampleInstance, null, 2);
  vm.isValid = false;
  vm.isInvalid = false;
  vm.submit = function() {
    $http({
      method: 'POST',
      url: '/validate',
      data: $httpParamSerializer({schema: vm.schema, instance: vm.instance}),
      headers: {'Content-Type': 'application/x-www-form-urlencoded'}
      }).then(
        function(result) {
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
