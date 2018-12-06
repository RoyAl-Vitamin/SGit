package vi.al.ro.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("repository")
public class Repository {

    private Logger logger = LoggerFactory.getLogger(Repository.class);

    /**
     * Возвращает всех пользователей
     * @return {@link vi.al.ro}'s
     */
    @RequestMapping(value = "/", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<?> getGroupEntireCollection() {
//        logger.info("#getGroupEntireCollection: get all");

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body("2");
    }
}
