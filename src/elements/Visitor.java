package elements;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Michiel Thomassen on 14/12/2016 at 18:11.
 * Project name: VisitorList
 * Package name: elements
 * File name: Visitor
 */
public class Visitor {
    private SimpleStringProperty date;
    private SimpleStringProperty name;
    private SimpleStringProperty municipality;
    private SimpleStringProperty copies;
    private SimpleStringProperty mail;
    private SimpleStringProperty taxes;
    private SimpleStringProperty others;

    public Visitor(String date, String name, String municipality, boolean copies, boolean mail, boolean taxes, boolean others) {
        this.date = new SimpleStringProperty(date);
        this.name = new SimpleStringProperty(name);
        this.municipality = new SimpleStringProperty(municipality);
        this.copies = new SimpleStringProperty("" + copies);
        this.mail = new SimpleStringProperty("" + mail);
        this.taxes = new SimpleStringProperty("" + taxes);
        this.others = new SimpleStringProperty("" + others);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getMunicipality() {
        return municipality.get();
    }

    public SimpleStringProperty municipalityProperty() {
        return municipality;
    }

    public String getCopies() {
        return copies.get();
    }

    public SimpleStringProperty copiesProperty() {
        return copies;
    }

    public String getMail() {
        return mail.get();
    }

    public SimpleStringProperty mailProperty() {
        return mail;
    }

    public String getTaxes() {
        return taxes.get();
    }

    public SimpleStringProperty taxesProperty() {
        return taxes;
    }

    public String getOthers() {
        return "" + others.get();
    }

    public SimpleStringProperty othersProperty() {
        return others;
    }
}
