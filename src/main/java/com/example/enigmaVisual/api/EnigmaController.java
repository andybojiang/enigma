package com.example.enigmaVisual.api;

import com.example.enigmaVisual.service.EnigmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.util.annotation.NonNull;

@RestController
public class EnigmaController {
    private final EnigmaService _enigmaService;
    @Autowired
    public EnigmaController(EnigmaService enigmaService) {
        this._enigmaService = enigmaService;
    }

    /**Process a post request to process a msg through the machine/
     * @param msg Message sent to machine from REST
     */
    @PostMapping
    public void process(@RequestBody @NonNull String msg) {
        //FIXME
    }

    /**Retrieves the message processed by the machine.
     */
    @GetMapping
    public String getMsg() {
        return "";
    }




}
