define([ "jquery", "uiGenerator" ], function($, UiGenerator) {

	var ErrorHandlers = (function() {
		
		var handleUnauthorizedRequest = function() {
			var URL = '/CRM/#!/login/msg/notlogged';
			window.location = URL;
		};
		
	    var errorHandlersMap = {
	    		//400: badRequestErrorHandler,
	    		401: handleUnauthorizedRequest,
	    		403: handleUnauthorizedRequest,
	    		//404: notFoundErrorHandler,
	    		//500: internalServerErrorHandler,
	    		//503: serviceUnavailableErrorHandler,
	    		};
		
		var handleGeneralError = function (error) {
			var errorCode = error.errorCode;
			var errorMessage = error.errorMessage;
			
			if (errorHandlersMap[errorCode]) {
				var errorHandler = errorHandlersMap[errorCode];
				errorHandler.call();
			} else {
				UiGenerator.showGeneralErrorMessage();
			}
		};
		
		return {
			'handleGeneralError': handleGeneralError,
			'handleUnauthorizedRequest': handleUnauthorizedRequest
		};
	})();
	
	return ErrorHandlers;
});