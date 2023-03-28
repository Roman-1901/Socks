package com.example.socks.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.socks.model.Color;
import com.example.socks.model.Size;
import com.example.socks.model.Socks;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class SocksService {
    private Map<Socks, Integer> socksMap = new HashMap<>();

    private final FileService fileService;

    public SocksService(FileService fileService) {
        this.fileService = fileService;
    }

    public String postSocks(Socks socks, int quantity) {
        if (quantity > 0 && socks.getCottonPart() >= 0) {
            socksMap.computeIfPresent(socks, (k, v) -> v + quantity);
            if (!socksMap.containsKey(socks)) {
                socksMap.put(socks, quantity);
            }
            saveToFile();
            return "Носки: цвет - " + socks.getColor().getNameColor() + ", размер - " + socks.getSize().getSizeNum() + " см, процент хлопка - " + socks.getCottonPart() + ", количество пар - " + quantity + " . Добавлены на склад";
        }
        return null;
    }


    public String getSocks(Color color, Size size, int cottonMin, int cottonMax) {
        int count = 0;
        if (cottonMin >= 0 && cottonMax >= 0 && cottonMax >= cottonMin) {
            for (Map.Entry<Socks, Integer> entry : socksMap.entrySet()) {
                if (entry.getKey().getColor() == color && entry.getKey().getSize() == size && entry.getKey().getCottonPart() >= cottonMin && entry.getKey().getCottonPart() <= cottonMax) {
                    count += entry.getValue();
                }
            }
            return "Носки: цвет - " + color.getNameColor() + ", размер - " + size.getSizeNum() + " см, минимальный процент хлопка - " + cottonMin + "%, максимальный процент хлопка - " + cottonMax + "%.  \n" +
                    "Количество на складе: " + count + " шт.";
        }
        return null;
    }

    public String putSocks(Socks socks, int quantity) {
        if (quantity > 0 && socksMap.containsKey(socks)) {
            int number = socksMap.get(socks) - quantity;
            if (number > 0) {
                socksMap.put(socks, number);
                saveToFile();
                return "Носки: цвет - " + socks.getColor().getNameColor() + ", размер - " + socks.getSize().getSizeNum() + " см. Отпущено со склада - " + quantity + " пар.\n" +
                        "Количество на складе: " + number + " шт.";
            } else if (number == 0) {
                socksMap.remove(socks);
                saveToFile();
                return "Носки: цвет - " + socks.getColor().getNameColor() + ", размер - " + socks.getSize().getSizeNum() + " см. Отпущено со склада - " + quantity + " пар.\n" +
                        "Количество на складе: 0 шт.";
            } else
                return "По данным параметрам носки отсутствуют, либо в запросе количество указано больше, чем имеется на складе";
        }
        return null;
    }

    public String deleteSocks(Socks socks, int quantity) {
        if (quantity > 0 && socksMap.containsKey(socks)) {
            int number = socksMap.get(socks) - quantity;
            if (number > 0) {
                socksMap.put(socks, number);
                saveToFile();
                return "Носки: цвет - " + socks.getColor().getNameColor() + ", размер - " + socks.getSize().getSizeNum() + " см. Удалено со склада - " + quantity + " пар.\n" +
                        "Количество на складе: " + number + " шт.";
            } else if (number == 0) {
                socksMap.remove(socks);
                saveToFile();
                return "Носки: цвет - " + socks.getColor().getNameColor() + ", размер - " + socks.getSize().getSizeNum() + " см. Удалено со склада - " + quantity + " пар.\n" +
                        "Количество на складе: 0 шт.";
            } else
                return "По данным параметрам носки отсутствуют, либо в запросе количество указано больше, чем имеется на складе";
        }
        return null;
    }



    private void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(socksMap);
            fileService.saveFile(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    // Данный метод чтения из файла к сожалению не работает, возможно это связано с тем, что объект Socks обозначен как ключ, а не как значение
    //   @PostConstruct
    private void readFromFile() {
        try {
            String json = fileService.readFromFile();
            socksMap = new ObjectMapper().readValue(json, new TypeReference<HashMap<Socks, Integer>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}

