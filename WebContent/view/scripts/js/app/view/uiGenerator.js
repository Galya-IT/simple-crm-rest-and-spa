define([ "jquery", "q", "handlebars", "tableSorter"], function($, Q, Handlebars) {

	var UiGenerator = (function() {
		Handlebars.registerPartial('top', $('#top-template').html());

		var HelperFunctions = {
			'MESSAGE_TYPES' : {
				ERROR : {
					defaultMessage: "Something went wrong. Please try again later.",
					className: 'error'
				},
				SUCCESS : {
					defaultMessage: "Success!",
					className: 'success'
				},
				WARNING : {
					defaultMessage: "Warning!",
					className: 'warning'
				},
			},
			
			'showMessage' : function(messageType, customMessage) {
				var isMessageTypeCorrect = false;
				
				$.each(HelperFunctions.MESSAGE_TYPES, function(index, item) {
					if (item === messageType) {
						isMessageTypeCorrect = true;
					}
				});
				
				if (!isMessageTypeCorrect) {
					throw new Exception("Incorrect message type.");
				}
				
				var message = customMessage || messageType.defaultMessage;
				var messageDiv = $('<div />').addClass(messageType.className).html(message);
				
				var LOGIN_DIV = $('.login-card');
				var TOP_MENU = $('#top');
				
				var insertedDiv = (LOGIN_DIV.length > 0) ? messageDiv.insertBefore(LOGIN_DIV) : messageDiv.insertAfter(TOP_MENU);

				insertedDiv.fadeOut(3000, 'swing', function() {
					$(this).remove();
				});
			},
			
			'renderHandlebarsTemplate' : function(templateContainerId, targetContainerId, dataName, data) {
				var templateContainer = document.getElementById(templateContainerId);
				var targetContainer = document.getElementById(targetContainerId);
				
				var rawTemplate = templateContainer.innerHTML;
				var compiledTemplate = Handlebars.compile(rawTemplate);
				
				while (targetContainer.firstChild) {
					targetContainer.removeChild(targetContainer.firstChild);
				}

				var jsonObjectPassedToHandlebars = { };
				
				if (dataName && data) {
					jsonObjectPassedToHandlebars[dataName] = data;
				}

				targetContainer.innerHTML = compiledTemplate(jsonObjectPassedToHandlebars);
			},

			'renderPageAsPromise' : function(pageTitle, templateName, container, dataName, data) {
	            var deferred = Q.defer();
	            
				document.title = pageTitle;
				HelperFunctions.renderHandlebarsTemplate(templateName, container, dataName, data);

				deferred.resolve();
				
	            return deferred.promise;
			},
			
			'deleteTableRowByDataId' : function(id) {
				var rowToDeleteSelector = "tr[data-id='" + id + "']";
				$(rowToDeleteSelector).remove();
			},
		};
		
		var showGeneralErrorMessage = function(customMessage) {
			HelperFunctions.showMessage(HelperFunctions.MESSAGE_TYPES.ERROR, customMessage);
		};
		
		var showGeneralSuccessMessage = function(customMessage) {
			HelperFunctions.showMessage(HelperFunctions.MESSAGE_TYPES.SUCCESS, customMessage);
		};

		var showGeneralWarningMessage = function(customMessage) {
			HelperFunctions.showMessage(HelperFunctions.MESSAGE_TYPES.WARNING, customMessage);
		};
		
		var visualizeAllClients = function(clientsArray) {
			var TITLE_ALL_CLIENTS_PAGE = 'All clients';
			
			document.title = TITLE_ALL_CLIENTS_PAGE;
			
			if (clientsArray.length === 0) {
				var divNoClients = $('<h1 />').html('No clients.');
				$('#wrapper').append(divNoClients);
			} else {
				HelperFunctions.renderHandlebarsTemplate('clients-template', 'clients-container', 'clients', clientsArray);
			}
		};
		
		var visualizeSingleClient = function(client) {
			var TITLE_PREVIEW_CLIENT_PAGE = 'Company preview - ' + client.name;
			var TEMPLATE_NAME_PREVIEW_CLIENT = 'client-template';
			var CONTAINER_PREVIEW_CLIENT = 'clients-container';
			var TEMPLATE_DATA_NAME = 'client';
			var TEMPLATE_DATA = client;
			
			return HelperFunctions.renderPageAsPromise(TITLE_PREVIEW_CLIENT_PAGE, TEMPLATE_NAME_PREVIEW_CLIENT, CONTAINER_PREVIEW_CLIENT, TEMPLATE_DATA_NAME, TEMPLATE_DATA);
		};
		
		var visualizeAddNewClient = function() {
			var TITLE_ADD_CLIENT_PAGE = 'Add new client';
			var TEMPLATE_NAME_ADD_CLIENT = 'add-edit-client-template';
			var CONTAINER_ADD_CLIENT = 'clients-container';
			
			return HelperFunctions.renderPageAsPromise(TITLE_ADD_CLIENT_PAGE, TEMPLATE_NAME_ADD_CLIENT, CONTAINER_ADD_CLIENT);
		};
		
		var visualizeEditClient = function(client) {
			var TITLE_EDIT_CLIENT_PAGE = 'Edit ' + client.name;
			var TEMPLATE_NAME_EDIT_CLIENT = 'add-edit-client-template';
			var CONTAINER_EDIT_CLIENT = 'clients-container';
			var TEMPLATE_DATA_NAME = 'client';
			var TEMPLATE_DATA = client;
			
			return HelperFunctions.renderPageAsPromise(TITLE_EDIT_CLIENT_PAGE, TEMPLATE_NAME_EDIT_CLIENT, CONTAINER_EDIT_CLIENT, TEMPLATE_DATA_NAME, TEMPLATE_DATA);
		};
		
		var visualizeLogin = function() {
			var TITLE_LOGIN_PAGE = 'CRM Log-in';
			var TEMPLATE_NAME_LOGIN = 'login-template';
			var CONTAINER_LOGIN = 'wrapper';
			
			return HelperFunctions.renderPageAsPromise(TITLE_LOGIN_PAGE, TEMPLATE_NAME_LOGIN, CONTAINER_LOGIN);
		};
		
		var showLoadingImage = function() {
			var loadingImage = $('<img />').attr('src', './view/img/loader.gif');
			var loadingImageDiv = $('<div />').addClass('loading').html(loadingImage);
			$('body').append(loadingImageDiv);
			
		};

		var hideLoadingImage = function() {
			var loadingImageDiv = $('.loading');
			
			if (loadingImageDiv) {
				loadingImageDiv.remove();
			}
		};
		
		var redirectToUrl = function(url) {
			window.location = url;
		};
		
		var redirectToIndex = function() {
			var INDEX_URL = '/CRM/#!/';
			window.location = INDEX_URL;
		};

		var redirectToLogin = function() {
			var LOGIN_URL = '/CRM/#!/login';
			window.location = LOGIN_URL;
		};
		
		var deleteClient = function (id) {
			HelperFunctions.deleteTableRowByDataId(id);
			showGeneralSuccessMessage("The client was deleted successfully.");
		};
		
		var redirectToIndexAndShowSuccess = function (successMessage) {
			redirectToIndex();
            showGeneralSuccessMessage(successMessage);
		};
		
		return {
			'showGeneralErrorMessage': showGeneralErrorMessage,
			'showGeneralSuccessMessage': showGeneralSuccessMessage,
			'showGeneralWarningMessage': showGeneralWarningMessage,
			'visualizeAllClients': visualizeAllClients,
			'visualizeSingleClient': visualizeSingleClient,
			'visualizeAddNewClient': visualizeAddNewClient,
			'visualizeEditClient': visualizeEditClient,
			'visualizeLogin': visualizeLogin,
			'showLoadingImage': showLoadingImage,
			'hideLoadingImage': hideLoadingImage,
			'deleteClient': deleteClient,
			'redirectToIndex': redirectToIndex,
			'redirectToLogin': redirectToLogin,
			'redirectToUrl': redirectToUrl,
			'redirectToIndexAndShowSuccess': redirectToIndexAndShowSuccess
		};
	})();
	
	return UiGenerator;
});