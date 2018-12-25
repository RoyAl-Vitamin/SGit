package vi.al.ro.api;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import vi.al.ro.configuration.RepoComponent;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("repository")
public class Repository {

    private Logger logger = LoggerFactory.getLogger(Repository.class);

    private final RepoComponent repo;

    public Repository(RepoComponent repo) {
        this.repo = repo;
    }

    @RequestMapping(value = "/setRepository", produces = "application/json", method = RequestMethod.POST)
    public ModelAndView addRepository(@RequestBody MultiValueMap<String, String> formData) {
        logger.info("Устанавливаем репо");

        String repoName = formData.getFirst("repo_name");
        String repoPath = formData.getFirst("repo_path");

        repo.setName(repoName);
        repo.setPath(repoPath);
        return new ModelAndView("forward:/listRepo");
    }

    @RequestMapping(
            value = "/set-repository",
            consumes = "application/json",
            produces = "application/json",
            method = RequestMethod.POST)
    public String addRepository(Param param) {
        logger.info("Устанавливаем репо");

        String repoName = param.getName();
        String repoPath = param.getPath();

        repo.setName(repoName);
        repo.setPath(repoPath);

        Map<String, String> map = new HashMap<>();
        map.put("status", "ok");
        return new Gson().toJson(map);
    }

    class Param {

        private String name;

        private String path;

        public Param() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
