package org.example.task_manager_work.services;

import org.example.task_manager_work.handlerException.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Service
public class JwtService {

    private final RestTemplate restTemplate;

    @Autowired
    public JwtService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String checkJWT(String token) throws UnauthorizedException {
        String validateUrl = "http://auth:8080/api/token/validate";

        try {

            String finalUrl = validateUrl + "?tokenRequest=" + token;
            ResponseEntity<String> response = restTemplate.getForEntity(finalUrl, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return token;
            }
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new UnauthorizedException("access токен истек, обновите его на сервисе авторизации");
            } else {
                throw new UnauthorizedException("Ошибка проверки токена: " + ex.getMessage());
            }
        }
        throw new UnauthorizedException("Не удалось проверить токен");
    }

    public String getData(String token) throws UnauthorizedException {
        String url = "http://auth:8080/api/token/getdata";


        String finalUrl = url + "?token=" + token;


        ResponseEntity<String> response = restTemplate.getForEntity(finalUrl, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new UnauthorizedException("Не удалось получить данные пользователя из токена");
        }

    }
}
