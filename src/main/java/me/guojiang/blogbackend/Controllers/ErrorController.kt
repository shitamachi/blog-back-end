package me.guojiang.blogbackend.Controllers

import me.guojiang.blogbackend.Models.JsonResult
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class ErrorController : ErrorController {

    @RequestMapping("/error")
    fun handlerError(request: HttpServletRequest): JsonResult<Map<String, String>> =
            when (val statusCode = request.getAttribute("javax.servlet.error.status_code")) {
                statusCode == 500 -> JsonResult.create(500, "server error", null)
                else -> JsonResult.create(500, "unknown error", null)
            }

    override fun getErrorPath(): String {
        return "/error"
    }
}