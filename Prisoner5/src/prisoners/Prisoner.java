package prisoners;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Prisoner implements Serializable {
    private static final long serialVersionUID = 1L; // ДОБАВЛЕНО

    private String fullName;
    private LocalDate birthDate;
    private int height;
    private String eyeColor;
    private List<String> features;
    private LocalDate dateIn;
    private LocalDate dateOut;

    public Prisoner(String fullName, LocalDate birthDate, int height, String eyeColor,
            List<String> features, LocalDate dateIn, LocalDate dateOut) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.height = height;
        this.eyeColor = eyeColor;
        this.features = new ArrayList<>(features);
        this.dateIn = dateIn;
        this.dateOut = dateOut;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public int getHeight() {
        return height;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public List<String> getFeatures() {
        return new ArrayList<>(features);
    }

    public LocalDate getDateIn() {
        return dateIn;
    }

    public LocalDate getDateOut() {
        return dateOut;
    }

    @Override
    public String toString() {
        String result = "  Name: " + fullName + "\n" +
                "  Birth: " + birthDate + "\n" +
                "  Height: " + height + "cm\n" +
                "  Eyes: " + eyeColor + "\n" +
                "  Features: " + features + "\n" +
                "  Imprisoned: " + dateIn + "\n" +
                "  Released: ";
        return result + (dateOut != null ? dateOut : "N/A") + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Prisoner))
            return false;
        Prisoner prisoner = (Prisoner) o;
        return Objects.equals(fullName, prisoner.fullName) &&
                Objects.equals(birthDate, prisoner.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, birthDate);
    }
}