define(["jquery", "q"], function ($, Q) {
    var ROOT_SERVER_URL = "http://localhost:8080/CRM/restapi/";
    var CLIENT_MANIPULATIONS_SERVER_URL = ROOT_SERVER_URL + "clients";
    var CLIENT_MANIPULATIONS_BY_ID_SERVER_URL = CLIENT_MANIPULATIONS_SERVER_URL + "?id=";
    var AUTH_SERVER_URL = ROOT_SERVER_URL + "login?checkSession";
    var LOGOUT_SERVER_URL = ROOT_SERVER_URL + "logout";

	var ServerOperations = (function() {
		
		var HelperFunctions = {
				
			'handleResponseStatus': function(responseJson) {
				var result = { };
				
	    		var statusMessage = responseJson.statusText;
	    		var statusCode = parseInt(responseJson.status);
	    		
	    		var firstNumberStatusCode = Math.floor(statusCode / 100);
	    		
	    		if (firstNumberStatusCode === 2) {
	    			result = { 'status': 1, 'data': { 'statusMessage': statusMessage } };
	        	} else {
	    			result = { 'status': 0, 'data': { 'errorCode' : statusCode, 'errorMessage' : statusMessage } };
	        	}
	    		
	    		return result;
			},
			
			'executeAjaxRequestAsPromise': function(httpMethod, url, data, isMultipart) {
				var deferred = Q.defer();
				
	        	var ajaxRequestBody = {
	            		url : url,
	        			type : httpMethod,
	        			data : data,
	            		dataType: 'json',
	            	};

            	if (isMultipart) {
            		//Options to tell jQuery not to process data or worry about content-type.
            		ajaxRequestBody['cache'] = false;
            		ajaxRequestBody['contentType'] = false;
            		ajaxRequestBody['processData'] = false;
            	} else {
            		ajaxRequestBody['contentType'] = 'application/json';
            	}
            	
                $.ajax(ajaxRequestBody).always(function(response) {
    				var responseJson = response;
    				
    				if (typeof(response) === 'String') {
    					responseJson = $.parseJSON(response);
    				}
    				
    				var ajaxRequestResult = HelperFunctions.handleResponseStatus(responseJson);
                    
                    if (ajaxRequestResult.status === 1) {
    	        		deferred.resolve(ajaxRequestResult.data.statusMessage);
                    } else {
                    	deferred.reject(ajaxRequestResult.data);
                    }
            	});
                	 
                return deferred.promise;
	    	},
	    		
	    	'executeGetJsonRequestAsPromise': function(url) {
				var deferred = Q.defer();
            	
				$.getJSON(url).always(function(response) {
					var ajaxRequestResult = HelperFunctions.handleResponseStatus(response);

					if (ajaxRequestResult.status === 1) {
		        		deferred.resolve(response);
	                } else {
	                	deferred.reject(ajaxRequestResult.data);
	                }
	        	});
				
                return deferred.promise;
			},

	    	'executeAjaxGetRequestAsPromise': function(url) {
	            var deferred = Q.defer();
	            
	        	$.ajax({
	            	url : url,
	    			type : 'GET',
	            })
	            .done(function() {
	        		deferred.resolve();
	        	})
	        	.fail(function() {
	        		deferred.reject();
	        	});
	        	
	            return deferred.promise;
			},
		};
		
		var getAllClients = function () {
        	var promise = HelperFunctions.executeGetJsonRequestAsPromise(CLIENT_MANIPULATIONS_SERVER_URL);
        	return promise;
        };
        
        var getClientById = function (id) {
            var url = CLIENT_MANIPULATIONS_BY_ID_SERVER_URL + id;
            
        	var promise = HelperFunctions.executeGetJsonRequestAsPromise(url);
        	return promise;
        };

        var deleteClientById = function (id) {
            var url = CLIENT_MANIPULATIONS_BY_ID_SERVER_URL + id;
			
            var data = {
            	'id' : id,
			};
            
            var isMultipart = false;
            
        	var promise = HelperFunctions.executeAjaxRequestAsPromise('DELETE', url, data, isMultipart);
        	return promise;
        };
        
        var updateClient = function(formData) {
            var isMultipart = true;
            
        	var promise = HelperFunctions.executeAjaxRequestAsPromise('PUT', CLIENT_MANIPULATIONS_SERVER_URL, formData, isMultipart);
        	return promise;
        };
        
        var addNewClient = function(formData) {
            var isMultipart = true;
            
        	var promise = HelperFunctions.executeAjaxRequestAsPromise('POST', CLIENT_MANIPULATIONS_SERVER_URL, formData, isMultipart);
        	return promise;
        };
        
        var checkIfSessionIsValid = function() {
        	var promise = HelperFunctions.executeAjaxGetRequestAsPromise(AUTH_SERVER_URL);
        	return promise;
        };
        
        var logout = function() {
        	var promise = HelperFunctions.executeAjaxGetRequestAsPromise(LOGOUT_SERVER_URL);
        	return promise;
        };
        
		return {
			'getAllClients': getAllClients,
			'getClientById': getClientById,
			'deleteClientById': deleteClientById,
			'updateClient': updateClient,
			'addNewClient': addNewClient,
			'checkIfSessionIsValid': checkIfSessionIsValid,
			'logout': logout
		};
	})();
	
	return ServerOperations;
});