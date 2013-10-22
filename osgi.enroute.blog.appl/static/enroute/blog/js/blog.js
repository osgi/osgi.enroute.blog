/**
 * This program implements a (simplistic) GUI for a Blogging server. The server
 * is rather simplistic that it only supports a single user. <p/> The
 * {@code @param} in this file provides the variables set in the scope.
 */
(function(angular) {
	var MODULE = angular.module("Blog", ['ngResource']);
	var Command, error, ok;
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

	// Configure the routing table, this dispatches the page requests

	MODULE.config(function($routeProvider) {
		$routeProvider.when('/', {
			templateUrl : '/enroute/blog/htm/home.htm',
			controller : Home
		});
		$routeProvider.when('/post/:id', {
			templateUrl : '/enroute/blog/htm/post/view.htm',
			controller : PostView
		});
		$routeProvider.when('/edit', {
			templateUrl : '/enroute/blog/htm/post/edit.htm',
			controller : PostNew
		});
		$routeProvider.when('/edit/:id', {
			templateUrl : '/enroute/blog/htm/post/edit.htm',
			controller : PostEdit
		});
		$routeProvider.when('/search', {
			templateUrl : '/enroute/blog/htm/post/search.htm', // ?query=<query>&from=<time>&limit=<limit>
			controller : PostSearch
		});
		$routeProvider.otherwise({
			redirectTo : '/'
		});
	});

	/**
	 * The controller for the home screen. We fetch the current posts.
	 * 
	 * @param posts
	 *            The promise for a list to hold the posts
	 */
	function Home($scope) {
		$scope.posts = [ EXAMPLE ]
	}

	/**
	 * Search posts page.
	 * 
	 * @param query
	 *            The query string
	 * @param from
	 *            The from time (for paging)
	 * @param limit
	 *            the number of items
	 */

	function PostSearch($scope, $routeParams) {
	}

	/**
	 * Show a post.
	 * 
	 * @param id
	 *            From the uri
	 * @param post
	 *            Promise for the current post
	 * @param edit
	 *            Function to start the edit of this post
	 * @param delete_
	 *            Function to delete this post (needs confirmation)
	 */

	function PostView($scope, $routeParams, $location) {
		$scope.post = EXAMPLE;

		$scope.edit = function() {
			$location.url("/edit/" + $routeParams.id);
		}

		$scope.delete_ = function() {
			if (confirm("You are sure you want to delete this post?"))
				$location.url("/");
		}
	}

	/**
	 * Edit a post (either new or existing)
	 * 
	 * @param save
	 *            Function to save the edited post
	 * @param reset
	 *            Function to rest the edited post
	 * @param cancel
	 *            Function to goto the home page
	 * @param isPristine
	 *            true if not changed
	 */

	function editPost($scope, $location, value) {

		$scope.save = function() {
			$location.url("/post/" + 1);
		}

		$scope.reset = function() {
			$scope.edit = angular.copy(value);
		}

		$scope.cancel = function() {
			$location.url("/");
		}

		$scope.isPristine = function() {
			return angular.equals($scope.edit, value);
		}

		$scope.reset();
	}

	/**
	 * Edit New Post Controller
	 * 
	 * @see editPost
	 */
	function PostNew($scope, $location) {
		editPost($scope, $location, {
			content : "",
			title : ""
		});
	}

	/**
	 * Edit Existing Post Controller
	 * 
	 * @see editPost
	 */
	function PostEdit($scope, $routeParams, $location) {
		editPost($scope, $location, EXAMPLE);
	}

	/**
	 * Main controller. Initialize the communication, etc.
	 * 
	 */

	window.enBlog = function($scope, $location, $resource, $route) {

		/*
		 * Error handler, sets $scope.message
		 */
		error = function(result) {
			$scope.message = "[" + result.status + "]";
		}

		ok = function(result) {
			$scope.message = "";
		}

		Command = $resource("/rest/command/:command", {
			command : '@command'
		});

		$scope.go = function(url) {
			$location.url(url);
		}

		$scope.search = function(query) {
			$location.url("/search");
			$location.search({
				query : query
			});
		}

		$scope.testdata = function() {
			Command.get({
				command : 'TESTDATA'
			}, function() {
				$route.reload();
				ok();
			}, error);
		}

		$scope.exception = function() {
			Command.get({
				command : 'EXCEPTION'
			}, ok, error);
		}

	}

	/**
	 * Add a filter that truncates a text. This is useful when you have a table
	 * and only want to show the first lines of text.
	 */
	MODULE.filter('truncate', function() {
		return function(input, l) {
			if (!input)
				return '';
			else if (l > input.length)
				return input;
			else
				return input.substring(0, l)
		}
	});

})(angular)
