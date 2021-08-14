package rafnews.backend.services;

import java.util.List;

import javax.inject.Inject;

import rafnews.backend.model.Keyword;
import rafnews.backend.repositories.keyword.IKeywordRepository;

public class KeywordService {
	@Inject
	private IKeywordRepository keywordRepository;

	public Keyword addKeyword(Keyword kw) {
		return this.keywordRepository.addKeyword(kw);
	}

	public List<Keyword> allKeywords(int page, int perPage) {
		return this.keywordRepository.allKeywords(page, perPage);
	}

	public Keyword findKeyword(String id) {
		return this.keywordRepository.findKeyword(id);
	}

	public Keyword findKeywordById(Integer id) {
		return this.keywordRepository.findKeywordById(id);
	}

	public void deleteKeyword(Integer id) {
		this.keywordRepository.deleteKeyword(id);
	}
}
