define([ "jquery", "q", "serverOperations", "uiGenerator", "errorHandlers", "eventHandlers", "inputDateValidator", "inputNameValidator" ],
		function($, Q, ServerOperations, UiGenerator, ErrorHandlers, EventHandlers, InputDateValidator, InputNameValidator) {
	
	var AppControlFlow = (function() {
		Q.stopUnhandledRejectionTracking();

		var addEditEventHandler = function(event) {
			var $this = $(event.target);
			var submitMethod = $this.data("method").trim();

			var formData = new FormData($('form')[0]);
			
			if (submitMethod === 'PUT') {
				ServerOperations.updateClient(formData).then(UiGenerator.redirectToIndexAndShowSuccess, ErrorHandlers.handleGeneralError);
			} else {
				ServerOperations.addNewClient(formData).then(UiGenerator.redirectToIndexAndShowSuccess, ErrorHandlers.handleGeneralError);
			}
		};
		
		var showAdminPage = function() {
			UiGenerator.showLoadingImage();
			
			ServerOperations.getAllClients()
				.then(function successHandler(response) {
							var clientsArray = response.data;
							UiGenerator.visualizeAllClients(clientsArray);
							
							$('#clients').tablesorter();
							$('#clients').on('click', '.preview', EventHandlers.previewEventHandler);
							$('#clients').on('click', '.edit', EventHandlers.editEventHandler);
							$('#clients').on('click', '.delete', EventHandlers.deleteEventHandler);
						}, ErrorHandlers.handleGeneralError)
				.done(function () {
							UiGenerator.hideLoadingImage();
						});
		};

		var showPreviewClientPage = function(clientId) {
			// incorrect id error show

			ServerOperations.getClientById(clientId).then(function successHandler(response) {
				var client = response.data;
				UiGenerator.visualizeSingleClient(client);

				$('#clients').on('click', '.edit', EventHandlers.editEventHandler);
				$('#clients').on('click', '.delete', EventHandlers.deleteEventHandler);
			}, ErrorHandlers.handleGeneralError);
		};
		
		var showAddNewClientPage = function() {
			UiGenerator.visualizeAddNewClient();
			
			$('.name').focus();
			InputDateValidator.init();
			InputNameValidator.init();
			
			$('#submit').on('click', addEditEventHandler);
		};

		var showEditClientPage = function(clientId) {
			// incorrect id error show

			ServerOperations.getClientById(clientId).then(function successHandler(response) {
				var client = response.data;
				
				UiGenerator.visualizeEditClient(client).then(function() {
					InputDateValidator.init();
					InputNameValidator.init();

					$('#submit').on('click', addEditEventHandler);
				});
			}, ErrorHandlers.handleGeneralError);
		};
		
		var showLoginPage = function(msgType) {
			$('head').append( $('<link rel="stylesheet" type="text/css" />').attr('href', 'http://codepen.io/assets/libs/fullpage/jquery-ui.css') );
			$('head').append( $('<link rel="stylesheet" type="text/css" />').attr('href', './view/css/login.css') );
			$('head').append( $('<script />').attr('src', 'http://codepen.io/assets/libs/fullpage/jquery_and_jqueryui.js') );

			UiGenerator.visualizeLogin();
			
			// TODO: Use dictionary instead of switch
			
			switch (msgType) {
				case "notlogged":
					UiGenerator.showGeneralErrorMessage("Please login first!");
					break;
				case "credentials":
					UiGenerator.showGeneralErrorMessage("Wrong username or password!");
					break;
				case "loggedout":
					UiGenerator.showGeneralSuccessMessage("You have successfully logged out!");
					break;
			}
		};
		
		return {
			'showAdminPage': showAdminPage,
			'showPreviewClientPage': showPreviewClientPage,
			'showAddNewClientPage': showAddNewClientPage,
			'showEditClientPage': showEditClientPage,
			'showLoginPage': showLoginPage,
		};
	})();
	
	return AppControlFlow;
});