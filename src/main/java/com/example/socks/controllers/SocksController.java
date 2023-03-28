package com.example.socks.controllers;

import com.example.socks.model.Color;
import com.example.socks.model.Size;
import com.example.socks.model.Socks;
import com.example.socks.services.SocksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/socks")
@Tag(name = "Учет носков на складе", description = "Приход/уход/списание")
public class SocksController {
    private final SocksService socksService;

    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }

    @PostMapping()
    @Operation(summary = "Приход носков на склад", description = "Необходимо указать количество носков и заполнить json-объект. Допустимые размеры: S(23), M(25), L(27), XL(29), XXL(31), XXXL(33). Допустимые цвета: RED(Красный), WHITE(Белый), BLUE(Синий), GREEN(Зеленый), BLACK(Черный), YELLOW(Желтый), ORANGE(Оранжевый)")
    @Parameters(
            value = {
                    @Parameter(
                            name = "quantity", example = "1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "удалось добавить приход"
            ),
            @ApiResponse(
                    responseCode = "400", description = "параметры запроса имеют некорректный формат"
            ),
            @ApiResponse(
                    responseCode = "404", description = "URL неверный или такого действия нет в веб-приложении"
            ),
            @ApiResponse(
                    responseCode = "500", description = "произошла ошибка, не зависящая от вызывающей стороны"
            )
    }
    )
    public ResponseEntity<String> addSocks(@RequestBody Socks socks, @RequestParam int quantity) {
        String post = socksService.postSocks(socks, quantity);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(post);
    }


    @GetMapping()
    @Operation(summary = "Вывод носков в диапазоне минимального и максимального количества хлопка", description = "Необходимо указать цвет, размер (S(23), M(25), L(27), XL(29), XXL(31), XXXL(33)), минимальный и максимальный процент хлопка")
    @Parameters(
            value = {
                    @Parameter(
                            name = "cottonMin", example = "0"
                    ),
                    @Parameter(
                            name = "cottonMax", example = "100"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "запрос выполнен, результат в теле ответа в виде строкового представления целого числа"
            ),
            @ApiResponse(
                    responseCode = "400", description = "параметры запроса имеют некорректный формат"
            ),
            @ApiResponse(
                    responseCode = "404", description = "URL неверный или такого действия нет в веб-приложении"
            ),
            @ApiResponse(
                    responseCode = "500", description = "произошла ошибка, не зависящая от вызывающей стороны"
            )
    }
    )
    public ResponseEntity<String> showSocks(@RequestParam Color color, @RequestParam Size size, @RequestParam int cottonMin, @RequestParam int cottonMax) {
        String get = socksService.getSocks(color, size, cottonMin, cottonMax);
        if (get == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(get);
    }


    @PutMapping()
    @Operation(summary = "Отпуск носков со склада", description = "Необходимо указать количество носков и заполнить json-объект. Допустимые размеры: S(23), M(25), L(27), XL(29), XXL(31), XXXL(33). Допустимые цвета: RED(Красный), WHITE(Белый), BLUE(Синий), GREEN(Зеленый), BLACK(Черный), YELLOW(Желтый), ORANGE(Оранжевый)")
    @Parameters(
            value = {
                    @Parameter(
                            name = "quantity", example = "1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "удалось произвести отпуск носков со склада, либо товара нет на складе"
            ),
            @ApiResponse(
                    responseCode = "400", description = "параметры запроса имеют некорректный формат"
            ),
            @ApiResponse(
                    responseCode = "404", description = "URL неверный или такого действия нет в веб-приложении"
            ),
            @ApiResponse(
                    responseCode = "500", description = "произошла ошибка, не зависящая от вызывающей стороны"
            )
    }
    )
    public ResponseEntity<String> PuttingSocks(@RequestBody Socks socks, @RequestParam int quantity) {
        String put = socksService.putSocks(socks, quantity);
        if (put == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(put);
    }

    @DeleteMapping()
    @Operation(summary = "Списание брака", description = "Необходимо указать количество носков и заполнить json-объект. Допустимые размеры: S(23), M(25), L(27), XL(29), XXL(31), XXXL(33). Допустимые цвета: RED(Красный), WHITE(Белый), BLUE(Синий), GREEN(Зеленый), BLACK(Черный), YELLOW(Желтый), ORANGE(Оранжевый)")
    @Parameters(
            value = {
                    @Parameter(
                            name = "quantity", example = "1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "товар списан со склада, либо товара нет на складе"
            ),
            @ApiResponse(
                    responseCode = "400", description = "параметры запроса имеют некорректный формат"
            ),
            @ApiResponse(
                    responseCode = "404", description = "URL неверный или такого действия нет в веб-приложении"
            ),
            @ApiResponse(
                    responseCode = "500", description = "произошла ошибка, не зависящая от вызывающей стороны"
            )
    }
    )
    public ResponseEntity<String> DelSocks(@RequestBody Socks socks, @RequestParam int quantity) {
        String del = socksService.deleteSocks(socks, quantity);
        if (del == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(del);
    }

}

