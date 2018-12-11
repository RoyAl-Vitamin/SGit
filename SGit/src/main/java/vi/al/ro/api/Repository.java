package vi.al.ro.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import vi.al.ro.beans.RepoBean;
import vi.al.ro.entity.Repo;

import java.util.Map;

@RestController
@RequestMapping("repository")
public class Repository {

    private Logger logger = LoggerFactory.getLogger(Repository.class);

    private final RepoBean repoBean;

    public Repository(RepoBean repoBean) {
        this.repoBean = repoBean;
    }

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

    /**
     * Возвращает всех пользователей
     * @return {@link vi.al.ro}'s
     */
    @RequestMapping(value = "/setRepository", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<?> setRepository() {
        logger.info("Устанавливаем репо");

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body("2");
    }

    @RequestMapping(value = "/addRepository", produces = "application/json", method = RequestMethod.POST)
    public ModelAndView addRepository(@RequestBody MultiValueMap<String, String> formData) {
        logger.info("Добавляем репо");

        String repoName = formData.getFirst("repo_name");
        String repoPath = formData.getFirst("repo_path");

        repoBean.addRepo(new Repo(repoName, repoPath));
        return new ModelAndView("forward:/listRepo");
    }
}
