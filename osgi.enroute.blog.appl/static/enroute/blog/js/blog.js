/**
 * This program implements a (simplistic) GUI for a Blogging server. The server
 * is rather simplistic that it only supports a single user. <p/> The
 * {@code @param} in this file provides the variables set in the scope.
 */
(function(angular) {
	var MODULE = angular.module("Blog", [ "ngResource" ]);
	var Post, Command, error;

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
		$scope.posts = Post.query(ok, error);
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
		$scope.posts = Post.query($routeParams, ok, error);
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
		$scope.post = Post.get($routeParams,ok,error);

		$scope.edit = function() {
			$location.url("/edit/" + $routeParams.id);
		}
		
		$scope.delete_ = function() {
			if ( confirm("You are sure you want to delete this post?") )
				$scope.post.$remove(function(d) {
					$location.url("/");
					ok();
				}, error);
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
			$scope.edit.$save(function(post) {
				$location.url("/post/" + post.id);
				ok();
			}, error);
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
		editPost($scope, $location, new Post({
			content : "",
			title : ""
		}));
	}

	/**
	 * Edit Existing Post Controller
	 * 
	 * @see editPost
	 */
	function PostEdit($scope, $routeParams, $location) {
		var post = Post.get($routeParams, function() {
			editPost($scope, $location, post);
			ok();
		}, error);
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

		Post = $resource("/rest/blogpost/:id", {
			id : '@id'
		});

		Command = $resource("/rest/command/:command", {command:'@command'});

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
			Command.get({command:'TESTDATA'},function() {
				$route.reload(); ok();
			}, error);
		}

		$scope.exception = function() {
			Command.get({command:'EXCEPTION'},ok, error);
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
