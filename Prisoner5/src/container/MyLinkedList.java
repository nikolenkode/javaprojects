package container;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

public class MyLinkedList<T> implements Serializable, Iterable<T> {
    private static final long serialVersionUID = 1L; // ДОБАВЛЕНО

    private Node<T> head;
    private int size;

    private static class Node<T> implements Serializable {
        private static final long serialVersionUID = 1L; // ДОБАВЛЕНО

        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

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

    public void clear() {
        head = null;
        size = 0;
    }

    public boolean contains(T element) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(element))
                return true;
            current = current.next;
        }
        return false;
    }

    public T get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

        Node<T> current = head;
        for (int i = 0; i < index; i++)
            current = current.next;
        return current.data;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

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
    public void saveToFileSerialized(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<T> {
        private Node<T> current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            T data = current.data;
            current = current.next;
            return data;
        }
    }

    // --- Фильтр ---
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