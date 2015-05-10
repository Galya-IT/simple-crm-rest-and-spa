define([ "jquery", "q", "serverOperations", "uiGenerator", "errorHandlers" ], function($, Q, ServerOperations, UiGenerator, ErrorHandlers) {
	
	var EventHandlers = (function() {
		Q.stopUnhandledRejectionTracking();
		
		var editEventHandler = function() {
			$target = $(this);
			var clientId = $target.data('id');
			
			var redirectClientEditUrl = '/CRM/#!/clients/edit/' + clientId;
			window.location = redirectClientEditUrl;
		};
		
		var deleteEventHandler = function() {
			$target = $(this);
			var clientId = $target.data('id');
	
			var isConfirmed = confirm('Are you sure you want to delete this item?');
			
			if (isConfirmed) {
				ServerOperations.deleteClientById(clientId).then(function successHandler() {
					UiGenerator.deleteClient(clientId);
					UiGenerator.showGeneralSuccessMessage(successMessage);
				}, ErrorHandlers.handleGeneralError);
			}
		};
		
		var previewEventHandler = function() {
			$target = $(this);
			var clientId = $target.data('id');

			var redirectClientEditUrl = '/CRM/#!/clients/preview/' + clientId;
			window.location = redirectClientEditUrl;
		};
		
		return {
			'editEventHandler': editEventHandler,
			'deleteEventHandler': deleteEventHandler,
			'previewEventHandler': previewEventHandler
		};
	})();
	
	return EventHandlers;
});