package ibf.project.mysterygame.utils;

import java.util.List;

import ibf.project.mysterygame.models.game.MysteryArticle;

public class MysteryArticleWrapper {
    private List<MysteryArticle> articles;

    public List<MysteryArticle> getMysteryArticles() {
        return articles;
    }

    public void setMysteryArticles(List<MysteryArticle> articles) {
        this.articles = articles;
    }
}
