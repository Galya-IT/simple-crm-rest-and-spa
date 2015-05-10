(function() {

	require.config({
		paths : {
			jquery : "lib/jquery-2.1.1.min",
			handlebars : "lib/handlebars-v1.3.0",
			q : "lib/q",
			sammy : "lib/sammy",
			tableSorter : "lib/jquery.tablesorter.min",
			appControlFlow : "app/controllers/app-control-flow",
			eventHandlers : "app/controllers/event-handlers",
			errorHandlers : "app/controllers/error-handlers",
			inputDateValidator : "app/controllers/input-date-validator",
			inputNameValidator : "app/controllers/input-name-validator",
			serverOperations : "app/model/server-operations",
			uiGenerator : "app/view/uiGenerator",
		},
		shim : {
			handlebars : {
				deps : [ 'jquery' ],
				exports : 'Handlebars'
			},
			sammy: {
				deps : [ 'jquery' ],
				exports : 'Sammy'
			}
		}
	});

	require([ "jquery", "sammy", "appControlFlow", "serverOperations", "errorHandlers", "uiGenerator" ],
			function($, Sammy, AppControlFlow, ServerOperations, ErrorHandlers, UiGenerator) {
		var MAIN_DIV = '#wrapper';
		var isUserLoggedIn = false;
		
		var app = Sammy(MAIN_DIV, function() {
			
			this.around(function(callback) {
				var isOnLoginPage = this.path.indexOf('login') > -1;
				
				if (!isUserLoggedIn && !isOnLoginPage) {
					ServerOperations.checkIfSessionIsValid().then(
							function validSessionHandler() {
								isUserLoggedIn = true;
								callback();
							}, function invalidSessionHandler() {
								UiGenerator.redirectToUrl('/CRM/#!/login/msg/notlogged');
							});
				} else if (isUserLoggedIn && isOnLoginPage) {
					UiGenerator.redirectToIndex();
				} else {
					callback();
				}
			});
			
			this.get("#!/", function() {
				AppControlFlow.showAdminPage();
			});

			this.get("#!/clients/preview/:id", function() {
				var clientIdString = this.params['id'].trim();
				var clientId = parseInt(clientIdString);
				AppControlFlow.showPreviewClientPage(clientId);
			});
			
			this.get("#!/clients/new", function() {
				AppControlFlow.showAddNewClientPage();
			});

			this.get("#!/clients/edit/:id", function() {
				var clientIdString = this.params['id'].trim();
				var clientId = parseInt(clientIdString);
				AppControlFlow.showEditClientPage(clientId);
			});

			this.get("#!/login", function() {
				AppControlFlow.showLoginPage();
			});

			this.get("#!/login/msg/:msgType", function() {
				var msgType = this.params['msgType'].trim();
				AppControlFlow.showLoginPage(msgType);
			});

			this.get("#!/logout", function() {
				ServerOperations.logout().then(function successHandler() {
					isUserLoggedIn = false;
					UiGenerator.redirectToUrl('/CRM/#!/login/msg/loggedout');
				}, function errorHandler() {
					UiGenerator.showGeneralErrorMessage("Problem occured, when trying to log out. Please try again later.");
				});
			});
			
			// TODO: 404 error page, but now is causing post button to stop working
			/*this.notFound = function(){
				// errorHandlers
			}*/
		});
		app.raise_errors = false;
		app.run("#!/");
	});
}());