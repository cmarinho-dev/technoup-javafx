package br.com.cmarinho.technoupjavafx.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class FileRepository<T extends Serializable> implements Repository<T> {
    private final Class<T> clazz;

    public FileRepository(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public List<T> findAll() {
        return readList();
    }

    @Override
    public void save(T entity) {
        ArrayList<T> items = readList();
        items.add(entity);
        saveList(items);
    }

    @Override
    public void update(T entity) {
        ArrayList<T> items = readList();
        int index = items.indexOf(entity);

        if (index >= 0) {
            items.set(index, entity);
            saveList(items);
        }
    }

    @Override
    public void delete(T entity) {
        ArrayList<T> items = readList();
        items.remove(entity);
        saveList(items);
    }

    private void saveList(ArrayList<T> items) {
        String filePath = clazz.getSimpleName() + ".dat";

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }

            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                outputStream.writeObject(items);
            }
        } catch (IOException e) {
            System.err.println("Error saving list: " + e.getMessage());
        }
    }

    private ArrayList<T> readList() {
        String filePath = clazz.getSimpleName() + ".dat";
        ArrayList<T> list = new ArrayList<>();

        File file = new File(filePath);
        if (!file.exists()) {
            return list;
        }

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            list = (ArrayList<T>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading list: " + e.getMessage());
        }

        return list;
    }
}
