package container;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

/**
 * Односвязный список, реализующий интерфейсы {@link Iterable} и
 * {@link Serializable}.
 * Поддерживает стандартные операции со списками, фильтрацию и сериализацию.
 * 
 * @param <T> тип элементов в списке
 * @version 1.0
 */
public class MyLinkedList<T> implements Serializable, Iterable<T> {
    /** Версия для сериализации */
    private static final long serialVersionUID = 1L; // ДОБАВЛЕНО

    /** Голова списка */
    private Node<T> head;
    /** Количество элементов в списке */
    private int size;

    /**
     * Внутренний класс для представления узла списка.
     * 
     * @param <T> тип данных в узле
     */
    private static class Node<T> implements Serializable {
        /** Версия для сериализации */
        private static final long serialVersionUID = 1L; // ДОБАВЛЕНО

        /** Данные узла */
        T data;
        /** Ссылка на следующий узел */
        Node<T> next;

        /**
         * Создает новый узел с указанными данными.
         * 
         * @param data данные для хранения в узле
         */
        Node(T data) {
            this.data = data;
        }
    }

    /**
     * Добавляет элемент в конец списка.
     * 
     * @param element элемент для добавления
     */
    public void add(T element) {
        Node<T> newNode = new Node<>(element);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    /**
     * Удаляет первое вхождение указанного элемента из списка.
     * 
     * @param element элемент для удаления
     * @return {@code true} если элемент был найден и удален, иначе {@code false}
     */
    public boolean remove(T element) {
        if (head == null)
            return false;

        if (head.data.equals(element)) {
            head = head.next;
            size--;
            return true;
        }

        Node<T> current = head;
        while (current.next != null) {
            if (current.next.data.equals(element)) {
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Удаляет элемент по указанному индексу.
     * 
     * @param index индекс элемента для удаления (отсчет с 0)
     * @return {@code true} если элемент был удален, иначе {@code false}
     */
    public boolean remove(int index) {
        if (index < 0 || index >= size)
            return false;

        if (index == 0) {
            head = head.next;
            size--;
            return true;
        }

        Node<T> current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }

        current.next = current.next.next;
        size--;
        return true;
    }

    /**
     * Удаляет все элементы из списка.
     */
    public void clear() {
        head = null;
        size = 0;
    }

    /**
     * Проверяет, содержит ли список указанный элемент.
     * 
     * @param element элемент для поиска
     * @return {@code true} если элемент найден, иначе {@code false}
     */
    public boolean contains(T element) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(element))
                return true;
            current = current.next;
        }
        return false;
    }

    /**
     * Возвращает элемент по указанному индексу.
     * 
     * @param index индекс элемента (отсчет с 0)
     * @return элемент по указанному индексу
     * @throws IndexOutOfBoundsException если индекс выходит за границы списка
     */
    public T get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

        Node<T> current = head;
        for (int i = 0; i < index; i++)
            current = current.next;
        return current.data;
    }

    /**
     * Возвращает количество элементов в списке.
     * 
     * @return размер списка
     */
    public int size() {
        return size;
    }

    /**
     * Проверяет, пуст ли список.
     * 
     * @return {@code true} если список пуст, иначе {@code false}
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Возвращает массив, содержащий все элементы списка.
     * 
     * @return массив элементов
     */
    public Object[] toArray() {
        Object[] array = new Object[size];
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            array[index++] = current.data;
            current = current.next;
        }
        return array;
    }

    // --- Сериализация объектов ---

    /**
     * Сохраняет список в бинарный файл через сериализацию.
     * 
     * @param filename имя файла для сохранения
     */
    public void saveToFileSerialized(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Загружает список из бинарного файла через десериализацию.
     * 
     * @param <T>      тип элементов в списке
     * @param filename имя файла для загрузки
     * @return загруженный список или пустой список при ошибке
     */
    @SuppressWarnings("unchecked")
    public static <T> MyLinkedList<T> loadFromFileSerialized(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (MyLinkedList<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new MyLinkedList<>();
        }
    }

    // --- Итератор ---

    /**
     * Возвращает итератор для обхода элементов списка.
     * 
     * @return итератор списка
     */
    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    /**
     * Внутренний класс итератора для обхода списка.
     */
    private class MyIterator implements Iterator<T> {
        /** Текущий узел */
        private Node<T> current = head;

        /**
         * Проверяет, есть ли следующий элемент.
         * 
         * @return {@code true} если есть следующий элемент, иначе {@code false}
         */
        @Override
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Возвращает следующий элемент списка.
         * 
         * @return следующий элемент
         */
        @Override
        public T next() {
            T data = current.data;
            current = current.next;
            return data;
        }
    }

    // --- Фильтр ---

    /**
     * Фильтрует список по указанному предикату.
     * 
     * @param predicate условие фильтрации
     * @return новый список с элементами, удовлетворяющими условию
     */
    public MyLinkedList<T> filter(Predicate<T> predicate) {
        MyLinkedList<T> result = new MyLinkedList<>();
        Node<T> current = head;
        while (current != null) {
            if (predicate.test(current.data))
                result.add(current.data);
            current = current.next;
        }
        return result;
    }

    // --- Сохранение в текстовый файл ---

    /**
     * Сохраняет список в текстовый файл.
     * Каждый элемент сохраняется в текстовом формате через toString().
     * 
     * @param filename имя текстового файла
     */
    public void saveToFileText(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            Node<T> current = head;
            while (current != null) {
                writer.println(current.data.toString());
                writer.println(); // пустая строка между объектами
                current = current.next;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Загрузка Prisoner из твоего текстового формата ---

    /**
     * Загружает список заключенных из текстового файла специального формата.
     * Формат файла: текстовые поля с префиксами, разделенные пустыми строками.
     * 
     * @param filename имя текстового файла
     * @return список заключенных
     */
    public static MyLinkedList<prisoners.Prisoner> loadPrisonersFromText(String filename) {
        MyLinkedList<prisoners.Prisoner> list = new MyLinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            String name = null;
            LocalDate birth = null;
            int height = 0;
            String eyes = null;
            List<String> features = new ArrayList<>();
            LocalDate dateIn = null;
            LocalDate dateOut = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    if (name != null) {
                        list.add(new prisoners.Prisoner(name, birth, height, eyes, features, dateIn, dateOut));
                    }
                    name = null;
                    birth = null;
                    height = 0;
                    eyes = null;
                    features = new ArrayList<>();
                    dateIn = null;
                    dateOut = null;
                    continue;
                }

                if (line.startsWith("Name:")) {
                    name = line.substring(5).trim();
                } else if (line.startsWith("Birth:")) {
                    birth = LocalDate.parse(line.substring(6).trim());
                } else if (line.startsWith("Height:")) {
                    height = Integer.parseInt(line.substring(7).replace("cm", "").trim());
                } else if (line.startsWith("Eyes:")) {
                    eyes = line.substring(5).trim();
                } else if (line.startsWith("Features:")) {
                    String feat = line.substring(9).trim();
                    if (feat.startsWith("[") && feat.endsWith("]"))
                        feat = feat.substring(1, feat.length() - 1);
                    features = feat.isEmpty() ? new ArrayList<>() : Arrays.asList(feat.split(",\\s*"));
                } else if (line.startsWith("Imprisoned:")) {
                    dateIn = LocalDate.parse(line.substring(11).trim());
                } else if (line.startsWith("Released:")) {
                    String val = line.substring(9).trim();
                    dateOut = val.equals("N/A") ? null : LocalDate.parse(val);
                }
            }

            // Последний объект, если файл не оканчивается пустой строкой
            if (name != null) {
                list.add(new prisoners.Prisoner(name, birth, height, eyes, features, dateIn, dateOut));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}