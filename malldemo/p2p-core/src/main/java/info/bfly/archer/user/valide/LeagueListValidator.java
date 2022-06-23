package info.bfly.archer.user.valide;

import java.util.Map;

public interface LeagueListValidator {
    void getleagueByid(Map<String, String> map) throws RuntimeException;

    void getleagueList(Map<String, String> map) throws RuntimeException;

    void save(Map<String, String> map) throws RuntimeException;
}
