var vesselApp = angular.module('VesselManagement', []);

vesselApp.controller('VesselController', ['$scope', '$http', function($scope, $http) {

	$scope.generateId = function() {
			return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
	}

	$scope.vessels = {}
	
	$scope.init = function(){
		$http.get('http://localhost:9000/api/vessels',
			{ 
				headers: {'content-type': 'application/json','accept':'application/json'}
			}
		).success(function(data) {
			$scope.vessels = data[0];
		});

	}

	$scope.addVessel = function(){
		console.log("Adding new vessel");
		$http.post('http://localhost:9000/api/vessels',
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
			}
		).success(function(data) {
			$scope.init();
		});
	}

	$scope.init();

}]);

