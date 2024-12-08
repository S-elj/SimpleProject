package selj.evogl.simpleproject.log_parser;

import jakarta.annotation.PostConstruct;
import org.json.JSONException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile("log_profiler")
public class LogProfileGenerator {
    @PostConstruct
    public void runLogAnalysis() {
        try {
            LogParser parser = new LogParser();
            parser.parseLogs("logs/user-logs.log");
            parser.saveProfilesToJson("logs/user-profiles.json");
            System.out.println("Profiles saved to output/user-profiles.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
