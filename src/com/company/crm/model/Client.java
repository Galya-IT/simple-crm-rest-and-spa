package com.company.crm.model;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.company.crm.exceptions.PropertyNotSetException;
import com.company.crm.exceptions.PropertyValidationException;
import com.company.crm.utils.ClientValidator;
import com.company.crm.utils.DateUtils;
import com.company.crm.utils.StringUtils;

@Entity
@Table (name="Clients")
public class Client implements Serializable {
    private static final long serialVersionUID = 7526472295622776147L;

    @Id
    @Column (name="Client_Id", updatable = false, unique = true, nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column (name="Client_Name", unique = true, nullable = false, length = 50)
    private String name;
    
    @Column (name="Client_Location", length = 50)
    private String location;
    
    @Column (name="Client_Notes")
    @Lob
    private String notes;
    
    @Column (name="Client_Contract_Date")
    @Temporal (TemporalType.DATE)
    private Date contractDate;
    
    @Column (name="Client_Logo_File_Name", length = 255)
    private String logoFileName;

    @Column (name="Client_Is_Deleted")
    private boolean isDeleted;

    public Client() {

    }

    public Client(String name) throws PropertyValidationException {
        this();
        setName(name);
    }

    public Client(String name, String location, String notes, String contractDate, String logoImageName) throws PropertyValidationException {
        this(name);

        setLocation(location);
        setNotes(notes);
        setContractDate(contractDate);
        setLogoFileName(logoImageName);
    }

    public Client(String name, String location, String notes, String contractDate, String logoImageName, int id) throws PropertyValidationException {
        this(name, location, notes, contractDate, logoImageName);

        setId(id);
    }

    public void setId(int id) throws PropertyValidationException {
        ClientValidator.validateId(id);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) throws PropertyValidationException {
        ClientValidator.validateName(name);
        this.name = name;
    }

    /**
     * @throws PropertyNotSetException
     *             Name is a required property for an object of type client.
     */
    public String getName() throws PropertyNotSetException {
        if (StringUtils.isNullOrEmpty(name)) {
            throw new PropertyNotSetException("The name property is not set.");
        }
        return name;
    }

    public void setLocation(String location) throws PropertyValidationException {
        ClientValidator.validateLocation(location);
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setNotes(String notes) throws PropertyValidationException {
        ClientValidator.validateNotes(notes);
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public void setContractDate(Date date) {
        this.contractDate = date;
    }
    
    @Transient
    public void setContractDate(String contractDate) throws PropertyValidationException {
        ClientValidator.validateContractDate(contractDate);

        try {
            this.contractDate = DateUtils.convertStringToDate(contractDate);
        } catch (ParseException e) {
            throw new PropertyValidationException("The value of date is not correct.");
        }
    }

    public Date getContractDate() {
        return contractDate;
    }

    @Transient
    public String getContractDate(String datePattern) throws ParseException {
        String contractDateInCustomFormat = DateUtils.convertDateToCustomFormat(contractDate, datePattern);
        return contractDateInCustomFormat;
    }

    public void setLogoFileName(String logoFileName) throws PropertyValidationException {
        ClientValidator.validateLogoFileName(logoFileName);
        this.logoFileName = logoFileName;
    }

    public String getLogoFileName() {
        return logoFileName;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }
    
    @Transient
    @Override
    public String toString() {
        // TO DO: Optimization, for loop (each field)
        StringBuilder resultBuilder = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        if (this.getId() > 0) {
            String id = String.format("Id: %s, ", this.id);
            resultBuilder.append(id);
        }

        if (this.name != null) {
            String name = String.format("Name: %s, ", this.name);
            resultBuilder.append(name);
        }

        if (this.location != null) {
            String location = String.format("Location: %s, ", this.location);
            resultBuilder.append(location);
        }

        if (this.notes != null) {
            String notes = String.format("Notes: %s, ", this.notes);
            resultBuilder.append(notes);
        }

        if (this.contractDate != null) {
            String contractDate = String.format("ContractDate: %s, ", this.contractDate);
            resultBuilder.append(contractDate);
        }

        if (this.logoFileName != null) {
            String logoName = String.format("Logo File Name: %s, ", this.logoFileName);
            resultBuilder.append(logoName);
        }

        String isDeleted = "Is Client Deleted: " + this.isDeleted;
        resultBuilder.append(isDeleted);
        resultBuilder.append(newLine);

        return resultBuilder.toString();
    }
}