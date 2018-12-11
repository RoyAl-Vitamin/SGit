package vi.al.ro.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vi.al.ro.beans.RepoBean;

@Configuration
public class RepoConfiguration {

    @Bean
    public RepoBean repoBeanConfigurer() {
        return new RepoBean();
    }
}
