var vesselApp = angular.module('VesselManagement', []);

var host = "localhost"
var port = "9000";
var protocol = "http";
var baseUri = protocol + "://" + host + ":" + port;

vesselApp.controller('VesselController', ['$scope', '$http', function($scope, $http) {

	$scope.generateId = function() {
		return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
	}

	$scope.notify = function(operation, operationClass) {
	  if (operationClass === "alert-success") {
	 		$scope.alertMessage = "The vessel `" + $scope.lastOperatedName + "` was successfully " + operation + "!";
	  } else {
			$scope.alertMessage = "The vessel `" + $scope.lastOperatedName + "` could not be " + operation +
			"! Please try again later!";
	  }
		$scope.operation = operation;
		$scope.operationClass = operationClass;
		$scope.operationAlert = true;
	}

	$scope.vessels = []
	
	$scope.init = function(){
		$http.get(baseUri + '/api/vessels',
			{
				headers: {'content-type': 'application/json','accept':'application/json'}
			}
		).success(function(data) {
			$scope.vessels = data;
		});
	}

	$scope.clearForm = function(){
		$scope.addVesselForm.$setPristine();
		$scope.id = undefined;
		$scope.name = "";
		$scope.width = "";
		$scope.length = "";
		$scope.draft = "";
		$scope.latitude = "";
		$scope.longitude = "";
	}

	$scope.addVessel = function(){
		console.log("Adding new vessel");
		$scope.lastOperatedName = angular.copy($scope.name);
		$http.post(baseUri + '/api/vessels',
			{
				"_id": $scope.name + "_" + $scope.generateId(),
				"name": $scope.name,
				"dimension": {
					"width": $scope.width,
					"length": $scope.length,
					"draft": $scope.draft,
				},
				"coordinates": {
					"latitude": $scope.latitude,
					"longitude": $scope.longitude
				}
			})
			.success(function(data) {
				$scope.init();
				$scope.clearForm();
				$scope.notify("added", "alert-success");
			})
			.error(function(responseData, status){
				 $scope.lastOperatedName = $scope.name;
				 $scope.notify("added", "alert-danger");
			});
	}

	$scope.deleteVessel = function(id, name){
		var result = confirm("Are you sure you want to delete this vessel?")
		if (result === true) {
			console.log("Deleting vessel with id: " + id);
			$scope.lastOperatedName = name;
			$http.delete(baseUri + '/api/vessels/' + id, "")
				.success(function(data) {
					$scope.notify("deleted", "alert-success");
					$scope.init();
				})
				.error(function(responseData, status){
					$scope.notify("deleted", "alert-danger");
					$scope.init();
				});
		}
	}

	$scope.editVessel = function(id){
		$http.get(baseUri + '/api/vessels/' + id,
			{
				headers: {'content-type': 'application/json','accept':'application/json'}
			}
		).success(function(data) {
			$scope.id = data._id;
			$scope.name = data.name;
			$scope.width = data.dimension.width;
			$scope.length = data.dimension.length;
			$scope.draft = data.dimension.draft;
			$scope.latitude = data.coordinates.latitude;
			$scope.longitude = data.coordinates.longitude;
		});
	}

	$scope.updateVessel = function(id){
		console.log("Updating vessel");
		$scope.lastOperatedName = angular.copy($scope.name);
		$http.put(baseUri + '/api/vessels/' + id,
			{
				"_id": id,
				"name": $scope.name,
				"dimension": {
						"width": $scope.width,
						"length": $scope.length,
						"draft": $scope.draft,
				},
				"coordinates": {
					"latitude": $scope.latitude,
					"longitude": $scope.longitude
				}
			})
			.success(function(data) {
				$scope.init();
				$scope.clearForm();
				$scope.notify("updated", "alert-success");
			})
			.error(function(responseData, status){
				 $scope.notify("updated", "alert-danger");
			});
	}

	$scope.init();

}]);

