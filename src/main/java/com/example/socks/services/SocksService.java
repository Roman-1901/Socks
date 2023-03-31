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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class SocksService {
    private HashMap<Socks, Integer> socksMap = new HashMap<>();

    private final FileService fileService;

    public SocksService(FileService fileService) {
        this.fileService = fileService;
    }

    private Integer getSocks(Socks socks) {
            return socksMap.get(socks);
    }

    public String postSocks(Socks socks, int quantity) {
        if (quantity > 0 && socks.getCottonPart() >= 0) {
            Integer count = getSocks(socks);
            if (count == null) {
                socksMap.put(socks, quantity);
            } else {
                count += quantity;
                socksMap.put(socks, count);
            }
            saveToFile();
            return "Носки: цвет - " + socks.getColor().getNameColor() + ", размер - " + socks.getSize().getSizeNum() + " см, процент хлопка - " + socks.getCottonPart() + ", количество пар - " + quantity + " . Добавлены на склад";
        }
        return null;
    }


    public Integer getSocks(Color color, Size size, int cottonMin, int cottonMax) {
        int count = 0;
        if (cottonMin >= 0 && cottonMax >= 0 && cottonMax >= cottonMin) {
            for (Map.Entry<Socks, Integer> entry : socksMap.entrySet()) {
                if (entry.getKey().getColor() == color && entry.getKey().getSize() == size && entry.getKey().getCottonPart() >= cottonMin && entry.getKey().getCottonPart() <= cottonMax) {
                    count += entry.getValue();
                }
            }
            return count;
        }
        return null;
    }

    //Я подумал, что здесь имеет смысл объединить PUT и DELETE в один метод, т.к. по сути функционал у них одинаковый.
    public Integer putOrDeleteSocks(Socks socks, int quantity) {
        if (quantity > 0 && socksMap.containsKey(socks)) {
            int number = socksMap.get(socks) - quantity;
            if (number > 0) {
                socksMap.put(socks, number);
                saveToFile();
                return number;
            } else if (number == 0) {
                socksMap.remove(socks);
                saveToFile();
                return number;
            } else
                return null;
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
//    @PostConstruct
    private void readFromFile() {
        try {
            String json = fileService.readFromFile();
            socksMap = new ObjectMapper().readValue(json, new TypeReference<HashMap<Socks, Integer>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}

