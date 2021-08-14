package rafnews.backend.repositories.keyword;

import java.util.List;

import rafnews.backend.model.Keyword;

public interface IKeywordRepository {

    public Keyword addKeyword(Keyword keyword);
    public List<Keyword> allKeywords(int page, int perPage);
    public Keyword findKeyword(String id);
    public Keyword findKeywordById(Integer id);
    public void deleteKeyword(Integer id);
}
