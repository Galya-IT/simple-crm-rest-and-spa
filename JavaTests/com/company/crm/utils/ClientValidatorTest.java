package com.company.crm.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import com.company.crm.CrmBaseTestClass;
import com.company.crm.controllers.database.ClientsOrmQueryManager;
import com.company.crm.exceptions.PropertyValidationException;
import com.company.crm.model.Client;

public class ClientValidatorTest extends CrmBaseTestClass {
    
    @Test
    public void checkIfNameIsUniqueMocking() throws PropertyValidationException {
        String CLIENT_NAME = "Gosho";
        Client CLIENT_GOSHO = new Client(CLIENT_NAME);
        
        List<Client> CLIENTS_WITH_NAME_GOSHO = new ArrayList<Client>();
        CLIENTS_WITH_NAME_GOSHO.add(CLIENT_GOSHO);
        
        ClientsOrmQueryManager fakePersistenceManager = EasyMock.createMock(ClientsOrmQueryManager.class);
        EasyMock.expect(fakePersistenceManager.getClienstWithName(CLIENT_NAME)).andReturn(CLIENTS_WITH_NAME_GOSHO);
        EasyMock.replay(fakePersistenceManager);
        
        boolean actualResult = ClientValidator.checkIfNameIsUnique(CLIENT_NAME, fakePersistenceManager);
        
        EasyMock.verify(fakePersistenceManager);
        
        boolean EXPECTED = false;
        Assert.assertEquals(EXPECTED, actualResult);
    }
    
    @Test
    public void validateClientAllPropertiesNoDuplicateNameCheckCorrectValues() throws SQLException {
        String NAME = "Company 1";
        String LOCATION = "Some location";
        String NOTES = "";
        String CONTRACT_DATE = "15.08.2014";
        String LOGO_FILE_NAME = "logoFileName.gif";
        boolean CHECK_FOR_DUPLICATE_NAME = false;
        boolean EXPECTED = true;

        validateAllPropertiesTest(NAME, LOCATION, NOTES, CONTRACT_DATE, LOGO_FILE_NAME, CHECK_FOR_DUPLICATE_NAME,
                EXPECTED);
    }

    @Test
    public void validateClientAllPropertiesNoDuplicateNameCheckNoName() throws SQLException {
        String NAME = "";
        String LOCATION = "Location";
        String NOTES = "Note 1";
        String CONTRACT_DATE = "15.12.2008";
        String LOGO_FILE_NAME = "some_logo_name.jpeg";
        boolean CHECK_FOR_DUPLICATE_NAME = false;
        boolean EXPECTED = false;

        validateAllPropertiesTest(NAME, LOCATION, NOTES, CONTRACT_DATE, LOGO_FILE_NAME, CHECK_FOR_DUPLICATE_NAME,
                EXPECTED);
    }

    @Test
    public void validateLogoFileNameCorrectJpegFileName() {
        String LOGO_FILENAME_JPEG = "logoFile.jpeg";
        boolean EXPECTED = true;
        validateLogoFileNameTest(LOGO_FILENAME_JPEG, EXPECTED);
    }

    @Test
    public void validateLogoFileNameCorrectSvgFileNameUpperCaseExtension() {
        String LOGO_FILENAME_JPEG = "logo_File.TIFF";
        boolean EXPECTED = true;
        validateLogoFileNameTest(LOGO_FILENAME_JPEG, EXPECTED);
    }

    @Test
    public void validateLogoFileNameIncorrectImageExtension1() {
        String LOGO_FILENAME_JPEG = "logoFilejpeg";
        boolean EXPECTED = false;
        validateLogoFileNameTest(LOGO_FILENAME_JPEG, EXPECTED);
    }

    @Test
    public void validateLogoFileNameIncorrectImageExtension2() {
        String LOGO_FILENAME_JPEG = "logoFile.png.com";
        boolean EXPECTED = false;
        validateLogoFileNameTest(LOGO_FILENAME_JPEG, EXPECTED);
    }

    @Test
    public void validateLogoFileNameIncorrectSymbols() {
        String LOGO_FILENAME_JPEG = "logo/:dsfs.png";
        boolean EXPECTED = false;
        validateLogoFileNameTest(LOGO_FILENAME_JPEG, EXPECTED);
    }

    @Test
    public void validateIdZeroValue() {
        int id = 0;
        boolean EXPECTED = false;
        validateIdTest(id, EXPECTED);
    }

    @Test
    public void validateIdNegativeValue() {
        int id = -44;
        boolean EXPECTED = false;
        validateIdTest(id, EXPECTED);
    }

    @Test
    public void validateIdPositiveValue() {
        int id = 975;
        boolean EXPECTED = true;
        validateIdTest(id, EXPECTED);
    }

    @Test
    public void validateNameEmpty() {
        String name = "";
        boolean EXPECTED = false;
        validateNameTest(name, EXPECTED);
    }

    @Test
    public void validateNameTooLong75Chars() {
        String name = "Adolph Blaine Charles David Earl Frederick Gerald Hubert Irvin John Kenneth";
        boolean EXPECTED = false;
        validateNameTest(name, EXPECTED);
    }

    @Test
    public void validateLocationEmpty() {
        String location = "";
        boolean EXPECTED = true;
        validateLocationTest(location, EXPECTED);
    }

    @Test
    public void validateLocationTooLong() {
        String location = "Some very long name of a place under the sun where everything happens the hard way";
        boolean EXPECTED = false;
        validateLocationTest(location, EXPECTED);
    }

    @Test
    public void validateNotesOver100000Chars() throws IOException {
        String FILE_NOTES_PATH = "JavaTests" + File.separator + "resources" + File.separator + "notes.txt";
        
        Path path = Paths.get(FILE_NOTES_PATH);
        StringBuilder stringBuilder = new StringBuilder();

        try (Stream<String> lines = Files.lines(path, Charset.forName("UTF-8"))) {
            lines.forEachOrdered(line -> stringBuilder.append(line));
        }

        String notes = stringBuilder.toString();

        boolean EXPECTED = false;
        validateNotesTest(notes, EXPECTED);
    }

    @Test
    public void validateContractDateInvalidFormat() {
        String date = "8.09.2012";
        boolean EXPECTED = false;
        validateContractDateTest(date, EXPECTED);
    }

    @Test
    public void validateContractDateCorrectFormatInvalidDate() {
        String date = "08.50.2012";
        boolean EXPECTED = false;
        validateContractDateTest(date, EXPECTED);
    }

    private void validateLogoFileNameTest(String fileName, boolean expectedResult) {
        boolean actualResult = ClientValidator.validateLogoFileName(fileName);
        Assert.assertEquals(expectedResult, actualResult);
    }

    private void validateIdTest(int id, boolean expectedResult) {
        boolean actualResult = ClientValidator.validateId(id);
        Assert.assertEquals(expectedResult, actualResult);
    }

    private void validateNameTest(String name, boolean expectedResult) {
        boolean actualResult = ClientValidator.validateName(name);
        Assert.assertEquals(expectedResult, actualResult);
    }

    private void validateNotesTest(String notes, boolean expectedResult) {
        boolean actualResult = ClientValidator.validateNotes(notes);
        Assert.assertEquals(expectedResult, actualResult);
    }

    private void validateContractDateTest(String date, boolean expectedResult) {
        boolean actualResult = ClientValidator.validateContractDate(date);
        Assert.assertEquals(expectedResult, actualResult);
    }

    private void validateLocationTest(String location, boolean expectedResult) {
        boolean actualResult = ClientValidator.validateLocation(location);
        Assert.assertEquals(expectedResult, actualResult);
    }

    /* private void checkIfNameIsUniqueTest(String name, boolean expectedResult) {
        boolean actualResult = ClientValidator.checkIfNameIsUnique(name);
        Assert.assertEquals(expectedResult, actualResult);
    } */

    private void validateAllPropertiesTest(String name, String location, String notes, String contractDate,
            String logoFileName, boolean checkForDuplicateName, boolean expectedResult) throws SQLException {
        boolean actualResult = ClientValidator.validateClientAllProperties(name, location, notes, contractDate,
                logoFileName, checkForDuplicateName);
        Assert.assertEquals(expectedResult, actualResult);
    }
}
