package vi.al.ro.beans;

import vi.al.ro.entity.Repo;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class RepoBean {

    private Set<Repo> repos = new HashSet<>();

    public Set<Repo> getRepos() {
        return repos;
    }

    public void setRepos(Set<Repo> repos) {
        this.repos = repos;
    }

    public Optional<Repo> getRepoByName(String repoName) {
        if (repoName == null) return Optional.empty();
        return repos.stream().filter(element -> repoName.equals(element.getName())).findFirst();
    }

    public boolean addRepo(Repo repo) {
        return repos.add(repo);
    }
}
