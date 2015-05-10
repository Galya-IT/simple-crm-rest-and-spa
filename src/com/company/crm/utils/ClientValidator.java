package com.company.crm.utils;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.company.crm.controllers.database.ClientsOrmQueryManager;
import com.company.crm.exceptions.PropertyValidationException;
import com.company.crm.model.Client;

public class ClientValidator {

    public static boolean validateClientAllProperties(String name, String location, String notes, String contractDate,
            String logoFileName, boolean checkForDuplicateName) throws SQLException {
        boolean isNameValid = validateName(name);
        boolean isLocationValid = validateLocation(location);
        boolean isNotesValid = validateNotes(notes);
        boolean isContractDateValid = validateContractDate(contractDate);
        boolean isLogoFileNameValid = validateLogoFileName(logoFileName);

        boolean isAllPropertiesValid = isNameValid && isLocationValid && isNotesValid && isContractDateValid
                && isLogoFileNameValid;
        return isAllPropertiesValid;
    }

    public static boolean checkIfNameIsUnique(String name, ClientsOrmQueryManager persistenceManager) {
        boolean isNameUnique = false;

        List<Client> clientsWithSameName = persistenceManager.getClienstWithName(name);

        if (clientsWithSameName == null || clientsWithSameName.size() == 0) {
            isNameUnique = true;
        }
        return isNameUnique;
    }

    public static boolean validateId(int id) {
        boolean isValid = true;

        if (id <= 0) {
            isValid = false;
        }

        return isValid;
    }

    public static boolean validateName(String name)  {
        boolean isValid = true;

        // TODO: add regex validation for inappropriate symbols
        
        if (StringUtils.isNullOrEmpty(name) || name.length() > 50) {
            isValid = false;
        }

        return isValid;
    }

    public static boolean validateLocation(String location) {
        boolean isValid = true;

        if (!StringUtils.isNullOrEmpty(location) && location.length() > 50) {
            isValid = false;
        }

        return isValid;
    }

    public static boolean validateNotes(String notes) {
        boolean isValid = true;

        if (!StringUtils.isNullOrEmpty(notes) && notes.length() > 100_000) {
            isValid = false;
        }

        return isValid;
    }

    public static boolean validateContractDate(String contractDate) {
        boolean isValid = false;

        if (!StringUtils.isNullOrEmpty(contractDate)) {
            String dateFormatWesternPattern = "\\d{4}-\\d{2}-\\d{2}";
            String dateFormatBulgarianPattern = "\\d{2}.\\d{2}.\\d{4}";

            try {
                SimpleDateFormat dateFormat = null;

                if (contractDate.matches(dateFormatWesternPattern)) {
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                } else if (contractDate.matches(dateFormatBulgarianPattern)) {
                    dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                } else {
                    throw new PropertyValidationException("The format of the date is not correct.");
                }

                dateFormat.setLenient(false);
                dateFormat.parse(contractDate);
                isValid = true;
            } catch (ParseException | PropertyValidationException e) {
                e.printStackTrace();
                isValid = false;
            }
        }

        return isValid;
    }

    public static boolean validateLogoFileName(String logoFileName) {
        boolean isValid = false;

        if (!StringUtils.isNullOrEmpty(logoFileName)) {
            String imagePattern = "([^\\//*&%:\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp|tiff))$)";
            Pattern pattern = Pattern.compile(imagePattern);
            Matcher matcher = pattern.matcher(logoFileName);

            if (matcher.matches()) {
                isValid = true;
            }
        }

        return isValid;
    }
}
