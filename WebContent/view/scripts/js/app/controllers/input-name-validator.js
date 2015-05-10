define([ "jquery" ], function($) {
	
	var InputNameValidator = (function() {

		var $nameInput = null;
		var $submitButton = null;
		var defaultBorderStyle = null;
		
		var init = function(nameInputSelector, submitButtonSelector) {
			
			if (nameInputSelector) {
				$nameInput = $(nameInputSelector);
			} else {
				$nameInput = $('.name');
			}
			
			if (submitButtonSelector) {
				$submitButton = $(submitButtonSelector);
			} else {
				$submitButton = $('#submit');
			}
			
			defaultBorderStyle = $nameInput.css('border');
			
			$nameInput.on('input', function(event) {
				var clientName = $(event.target).val().trim();
				checkIfValidNameLength(clientName);
			});
			
			checkIfValidNameLength($nameInput.val());
		};
		
		
		var checkIfValidNameLength = function(name) {
			var isNameLongEnough = false;

			if (name !== undefined && name !== null && name !== '') {
				isNameLongEnough = name.length > 2;
			}
			
			if (!isNameLongEnough) {
				$nameInput.css('border', '2px solid red');
				$submitButton.attr('disabled', 'disabled');
			} else {
				$nameInput.css('border', defaultBorderStyle);
				if ($submitButton.attr('disabled') !== undefined) {
					$submitButton.removeAttr('disabled');
				}
			}
		};
		
		return {
			'init': init,
		};
	})();
	
	return InputNameValidator;
});