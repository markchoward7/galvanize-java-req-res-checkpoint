package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() { return "home"; }

    @GetMapping("/camelize")
    public String camelize(@RequestParam(name = "original", required = true) String original,
                           @RequestParam(name = "initialCap", defaultValue = "false") boolean initialCap) {

        if (initialCap) {
            original = original.substring(0,1).toUpperCase() + original.substring(1);
        }
        while (original.contains("_")) {
            int index = original.indexOf("_");
            String nextChar = original.substring(index+1, index+2);
            original = original.substring(0, index) + nextChar.toUpperCase() + original.substring(index+2);
        }
        return original;
    }

    @GetMapping("/redact")
    public String redact(@RequestParam(name = "original") String original,
                         @RequestParam(name = "badWord") String[] badWords) {

        String[] words = original.split(" ");
        for (int i = 0; i < words.length; i++) {
            for (String badWord : badWords) {
                if (words[i].equals(badWord)) {
                    words[i] = "";
                    for (int j = 0; j < badWord.length(); j++) {
                        words[i] += "*";
                    }
                    break;
                }
            }
        }
        original = String.join(" ", words);
        return original;
    }

    @PostMapping("/encode")
    public String encode(@RequestParam(name = "message") String message,
                         @RequestParam(name = "key") String key) {

        String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
        String result = "";
        for (char ch : message.toCharArray()) {
            if (ch == ' ') {
                result += " ";
                continue;
            }
            int index = ALPHABET.indexOf(ch);
            result += key.substring(index, index+1);
        }
        return result;
    }

    @PostMapping("/s/{find}/{replacement}")
    public String sed(@PathVariable(name = "find") String find,
                      @PathVariable(name = "replacement") String replacement,
                      @RequestBody() String message) {

        return message.replaceAll(find, replacement);
    }
}
