package ru.clevertec.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.service.RequestMethod;
import ru.clevertec.service.model.state.Fail;
import ru.clevertec.service.model.state.Result;
import ru.clevertec.service.model.state.Success;
import ru.clevertec.service.product.ProductService;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<String> getPagingData(@RequestParam("last_index") Long lastId, @RequestParam("page_size") Integer pageSize){
        String query = "last_index=" + lastId + "&page_size=" + pageSize;
        Result<String> products = productService.handleRequest(query, RequestMethod.GET);
        return getResponse(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable Long id){
        Result<String> product = productService.handleRequest("id=" + id, RequestMethod.GET);
        return getResponse(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        Result<String> result = productService.handleRequest("id=" + id, RequestMethod.DELETE);
        return getResponse(result);
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody String product){
        Result<String> result = productService.handleRequest(product, RequestMethod.PUT);
        return getResponse(result);
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody String product){
        Result<String> result = productService.handleRequest(product, RequestMethod.POST);
        return getResponse(result);
    }

    private ResponseEntity<String> getResponse(Result<String> result) {
        if(result.getClass() == Success.class) {
            return ResponseEntity.ok(((Success<String>) result).getData());
        } else {
            Fail fail = (Fail) result;
            return  new ResponseEntity<>(fail.getMessage(), HttpStatus.valueOf(fail.getErrorCode()));
        }
    }
}
