package net.toxbank.isa.creator.plugin.resource;

/**
 * Created by the ISA team
 *
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 *         <p/>
 *         Date: 10/05/2012
 *         Time: 11:24
 */
public class ResourceField {
    
    private String fieldName, assayTechnology, assayMeasurement;

    public ResourceField(String fieldName, String assayTechnology, String assayMeasurement) {
        this.fieldName = fieldName;
        this.assayTechnology = assayTechnology;
        this.assayMeasurement = assayMeasurement;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getAssayTechnology() {
        return assayTechnology;
    }

    public String getAssayMeasurement() {
        return assayMeasurement;
    }

    @Override
    public String toString() {
        return "ResourceField{" +
                "fieldName='" + fieldName + '\'' +
                ", assayTechnology='" + assayTechnology + '\'' +
                ", assayMeasurement='" + assayMeasurement + '\'' +
                '}';
    }
}
