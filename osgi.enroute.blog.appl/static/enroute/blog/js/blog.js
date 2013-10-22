/**
 * This program implements a (simplistic) GUI for a Blogging server. The server
 * is rather simplistic that it only supports a single user. <p/> The
 * {@code @param} in this file provides the variables set in the scope.
 */
(function(angular) {
	var MODULE = angular.module("Blog", []);
	var EXAMPLE = {
		id : 1,
		title : "title",
		created : new Date().getTime(),
		lastModified : new Date().getTime(),
		content : "There was a table set out under a tree in front of the house, and the March Hare and "
				+ "the Hatter were having tea at it: a Dormouse was sitting between them, fast asleep, and the other two were using it as a cushion, resting their elbows on it, and talking "
				+ "over its head. 'Very uncomfortable for the Dormouse,' thought Alice; 'only, as it's asleep, I suppose it doesn't mind.'\nThe table was a large one, but the three were all "
				+ "crowded together at one corner of it: 'No room! No room!' they cried out when they saw Alice coming. 'There's PLENTY of room!' said Alice indignantly, and she sat"
				+ " down in a large arm-chair at one end of the table.\n'Have some wine,' the March Hare said in an encouraging tone. Alice looked all round the table, but there was nothing on it but "
				+ "tea. 'I don't see any wine,' she remarked.\n'There isn't any,' said the March Hare.\n'Then it wasn't very civil of you to offer it,' said Alice angrily."
	};

	/**
	 * Main controller. Initialize the communication, etc.
	 * 
	 */

	window.enBlog = function($scope, $location, $route) {
		$scope.posts = []

		$scope.save = function() {
			$scope.posts.push({
				title : $scope.title,
				content : $scope.content,
				created : new Date().getTime()
			});
			$scope.title = $scope.content = "";
		}

		$scope.remove = function(post) {
			$scope.posts.splice($scope.posts.indexOf(post), 1);
		}
		
		$scope.testdata = function() {
			$scope.title = EXAMPLE.title;
			$scope.content = EXAMPLE.content;
		}
	}

})(angular)
