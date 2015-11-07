function MyCtrl($scope, $parse) {
    var validate = function() {
        $http.get("")
    }
    $scope.submit = function(){
        var serverResponse = pretendThisIsOnTheServerAndCalledViaAjax();

        for (var fieldName in serverResponse) {
            var message = serverResponse[fieldName];
            var serverMessage = $parse('myForm.'+fieldName+'.$error.serverMessage');

            if (message == 'VALID') {
                $scope.myForm.$setValidity(fieldName, true, $scope.myForm);
                serverMessage.assign($scope, undefined);
            }
            else {
                $scope.myForm.$setValidity(fieldName, false, $scope.myForm);
                serverMessage.assign($scope, serverResponse[fieldName]);
            }
        }
    };
}