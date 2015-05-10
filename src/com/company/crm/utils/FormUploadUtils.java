package com.company.crm.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

import com.company.crm.exceptions.ImageAlreadyExistsException;
import com.company.crm.exceptions.InvalidFormException;
import com.company.crm.exceptions.PropertyValidationException;
import com.company.crm.model.Client;

public class FormUploadUtils {
    public static String getTextFieldValue(HttpServletRequest request, String textFieldName) throws IOException,
            ServletException {
        Part part = request.getPart(textFieldName);

        return getTextFieldValue(part);
    }

    public static String getTextFieldValue(Part part) throws IOException, ServletException {
        InputStreamReader inputStreamReader = new InputStreamReader(part.getInputStream(), "UTF-8");
        BufferedReader reader = new BufferedReader(inputStreamReader);

        StringBuilder value = new StringBuilder();
        char[] buffer = new char[1024];

        for (int length = 0; (length = reader.read(buffer)) > 0;) {
            value.append(buffer, 0, length);
        }

        return value.toString();
    }

    public static Client populateClientWithFormData(Client client, HttpServletRequest request) throws IOException, ServletException, InvalidFormException, ImageAlreadyExistsException {
        for (FormFieldType fieldType : FormFieldType.values()) {
            String fieldName = fieldType.toString();
            
            Part part = request.getPart(fieldName);
            String value = getTextFieldValue(part).trim();

            /** 
             * TODO: check if null or empty, if so, don't set
             */
            try {
                switch (fieldType) {
                    
                    case NAME:
                        client.setName(value);
                        break;
                    case LOCATION:
                        boolean isLocationValid = ClientValidator.validateNotes(value);
                    
                        if (isLocationValid) {
                            client.setLocation(value);
                        }
                        break;
                    case NOTES:
                        boolean isNotesValid = ClientValidator.validateNotes(value);
                    
                        if (isNotesValid) {
                            client.setNotes(value);
                        }
                        break;
                    case CONTRACT_DATE:
                        boolean isDateValid = ClientValidator.validateContractDate(value);
                    
                        if (isDateValid) {
                            try {
                                value = DateUtils.convertWesternToBulgarianDateFormat(value);
                                client.setContractDate(value);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                throw new InvalidFormException("Client date is not in correct format.");
                            }
                        }
                        break;
                    case LOGO_FILE:
                        String logoFileName = getUploadFileName(part).trim().toLowerCase();
                        
                        if (!StringUtils.isNullOrEmpty(logoFileName)) {
                            boolean isUploadImageValid = validateImageFile(part);

                            if (isUploadImageValid) {
                                // TODO: rename file appropriately
                                client.setLogoFileName(logoFileName);

                                // TODO: move logic for file save after successful client add/update in database
                                String LOGOS_FOLDER_RELATIVE_PATH = "WEB-INF" + File.separator + "logos" + File.separator;
                                String fileAbsolutePath = getAbsolutePath(request, LOGOS_FOLDER_RELATIVE_PATH, logoFileName);
                                savePartToDisk(part, fileAbsolutePath);
                            } else {
                                throw new InvalidFormException("Client logo is missing or is not valid image format.");
                            }
                        }
                        break;
                }
            } catch (PropertyValidationException e) {
                e.printStackTrace();
                throw new InvalidFormException(e.getMessage());
            }
        }

        return client;
    }

    public static boolean validateImageFile(Part imageFileFormPart) {
        boolean isCorrectImage = false;

        long logoFileSize = imageFileFormPart.getSize();
        String logoFileType = FormUploadUtils.getUploadFileMimeType(imageFileFormPart);

        if (logoFileType.equals("image") && logoFileSize > 0) {
            isCorrectImage = true;
        }

        return isCorrectImage;
    }

    /**
     * Method used to get uploaded file name. This will parse following string
     * and get filename Content-Disposition: form-data; name="content";
     * filename="a.txt"
     **/
    public static String getUploadFileName(Part part) {
        String fileName = "";
        String header = "content-disposition";

        String[] headerInfoArray = part.getHeader(header).split(";");

        for (String headerInfo : headerInfoArray) {
            if (headerInfo.trim().startsWith("filename")) {
                fileName = headerInfo.substring(headerInfo.indexOf('=') + 1);
                fileName = fileName.trim().replace("\"", "");
            }
        }

        return fileName;
    }

    public static String getUploadFileMimeType(Part part) {
        String header = "content-type";

        String mimeType = part.getHeader(header);
        return mimeType.split("/")[0];
    }

    public static void savePartToDisk(Part part, String fileAbsolutePath) throws ImageAlreadyExistsException,
            IOException {
        File file = new File(fileAbsolutePath);

        if (!file.exists()) {
            file.createNewFile();
        } else {
            throw new ImageAlreadyExistsException("Uploaded image already exists.");
        }

        InputStream in = part.getInputStream();
        FileOutputStream out = new FileOutputStream(file);
        IOUtils.copy(in, out);
    }

    public static String getAbsolutePath(HttpServletRequest request, String folder, String fileName) {
        HttpSession session = request.getSession();
        ServletContext context = session.getServletContext();
        String realContextPath = context.getRealPath(request.getContextPath());

        String parentDirectory = new File(realContextPath).getParent();
        String filePath = parentDirectory + File.separator + folder;

        String fileAbsolutePath = filePath.concat(fileName);

        return fileAbsolutePath;
    }

}
