define([ "jquery" ], function($) {
	
	var InputDateValidator = (function() {
		
		var $dateInput = null;
		var $submitButton = null;
		var defaultBorderStyle = null;
		var currentYear = null;
		
		var init = function(dateInputSelector, submitButtonSelector) {
			if (dateInputSelector) {
				$dateInput = $(dateInputSelector);
			} else {
				$dateInput = $('.date');
			}
			
			if (submitButtonSelector) {
				$submitButton = $(submitButtonSelector);
			} else {
				$submitButton = $('#submit');
			}
			
			defaultBorderStyle = $dateInput.css('border');
			currentYear = (new Date).getFullYear();
			
			$dateInput.on('input', function() {
				var inputDate = $dateInput.val().trim();
				
				var isValidDate1 = isValidDayMonthDate(inputDate);
				var isValidDate2 = isValidDayMonthYearDate(inputDate);
				
				if (inputDate === '' || isValidDate1 || isValidDate2) {
					$dateInput.css('border', defaultBorderStyle);
					
					if ($submitButton.attr('disabled')) {
						$submitButton.removeAttr('disabled');
					}
				} else {
					$dateInput.css('border', '2px solid red');
					$submitButton.attr('disabled', 'disabled');
				}
			});
			
			$dateInput.on('focusout', function() {
				var inputDate = $dateInput.val().trim();
				
				if (isValidDayMonthDate(inputDate)) {
					var autoCompletedDate = inputDate + '.' + currentYear;
					$dateInput.val(autoCompletedDate);
				}
			});
			
			var intialDate = $dateInput.val().trim();

			if (intialDate === undefined || intialDate === null || intialDate === '') {
				// TODO: disable send button
			}
		};
		
		var checkIfValidDate = function(day, month, year) {
			var isValidDate = false;
			
			if (month >= 1 && month <= 12) {
				var daysInMonth = new Date(year, month, 0).getDate();
	
				if (day <= daysInMonth) {
					isValidDate = true;
				}
			}
			return isValidDate;
		};
	
		// checks for date in format 00.00
		var isValidDayMonthDate = function(inputDate) {
			var isValidDate = false;
			
			if (inputDate.match(/^\d\d\.\d\d$/)) {
				var inputDayMonthArray = inputDate.split('.');
				var inputDay = parseInt(inputDayMonthArray[0]);
				var inputMonth = parseInt(inputDayMonthArray[1]);
	
				isValidDate = checkIfValidDate(inputDay, inputMonth, currentYear);
			}
			
			return isValidDate;
		};
	
		// checks for date in format 00.00.0000
		var isValidDayMonthYearDate = function(inputDate) {
			var isValidDate = false;
			
			if (inputDate.match(/^\d\d\.\d\d\.\d\d\d\d$/)) {
				var inputDayMonthYearArray = inputDate.split('.');
				var inputDay = parseInt(inputDayMonthYearArray[0]);
				var inputMonth = parseInt(inputDayMonthYearArray[1]);
				var inputYear = parseInt(inputDayMonthYearArray[2]);
	
				isValidDate = checkIfValidDate(inputDay, inputMonth, inputYear);
			}
			
			return isValidDate;
		};
		
		return {
			'init': init,
		};
	})();
	
	return InputDateValidator;
});