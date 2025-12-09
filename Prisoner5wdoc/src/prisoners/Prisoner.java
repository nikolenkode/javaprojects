package prisoners;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * Класс, представляющий заключенного.
 * Содержит личные данные и информацию о заключении.
 * Реализует интерфейс {@link Serializable} для поддержки сериализации.
 * 
 * @version 1.0
 */
public class Prisoner implements Serializable {
    /** Версия для сериализации */
    private static final long serialVersionUID = 1L; // ДОБАВЛЕНО

    /** Полное имя заключенного */
    private String fullName;
    /** Дата рождения */
    private LocalDate birthDate;
    /** Рост в сантиметрах */
    private int height;
    /** Цвет глаз */
    private String eyeColor;
    /** Особые приметы */
    private List<String> features;
    /** Дата заключения под стражу */
    private LocalDate dateIn;
    /** Дата освобождения (null если еще не освобожден) */
    private LocalDate dateOut;

    /**
     * Создает нового заключенного с указанными данными.
     * 
     * @param fullName  полное имя
     * @param birthDate дата рождения
     * @param height    рост в сантиметрах
     * @param eyeColor  цвет глаз
     * @param features  список особых примет
     * @param dateIn    дата заключения
     * @param dateOut   дата освобождения (может быть null)
     */
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

    /**
     * Возвращает полное имя заключенного.
     * 
     * @return полное имя
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Возвращает дату рождения заключенного.
     * 
     * @return дата рождения
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Возвращает рост заключенного.
     * 
     * @return рост в сантиметрах
     */
    public int getHeight() {
        return height;
    }

    /**
     * Возвращает цвет глаз заключенного.
     * 
     * @return цвет глаз
     */
    public String getEyeColor() {
        return eyeColor;
    }

    /**
     * Возвращает копию списка особых примет.
     * 
     * @return список особых примет (неизменяемая копия)
     */
    public List<String> getFeatures() {
        return new ArrayList<>(features);
    }

    /**
     * Возвращает дату заключения.
     * 
     * @return дата заключения
     */
    public LocalDate getDateIn() {
        return dateIn;
    }

    /**
     * Возвращает дату освобождения.
     * 
     * @return дата освобождения или null если заключенный еще не освобожден
     */
    public LocalDate getDateOut() {
        return dateOut;
    }

    /**
     * Возвращает строковое представление заключенного.
     * Формат включает все поля с читаемыми названиями.
     * 
     * @return строковое представление
     */
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

    /**
     * Сравнивает заключенных по имени и дате рождения.
     * 
     * @param o объект для сравнения
     * @return {@code true} если объекты равны, иначе {@code false}
     */
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

    /**
     * Возвращает хэш-код на основе имени и даты рождения.
     * 
     * @return хэш-код
     */
    @Override
    public int hashCode() {
        return Objects.hash(fullName, birthDate);
    }
}