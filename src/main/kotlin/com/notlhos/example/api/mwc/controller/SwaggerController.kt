package com.notlhos.example.api.mwc.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
@RequestMapping("/")
class SwaggerController {

    @RequestMapping(method = [ RequestMethod.GET ])
    fun swaggerMain(): String {
        return "redirect:/swagger-ui.html"
    }

}