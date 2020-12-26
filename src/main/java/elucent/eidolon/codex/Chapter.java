package elucent.eidolon.codex;

import java.util.List;

import com.google.common.collect.Lists;

public class Chapter {
    String titleKey;
    List<Page> pages;

    public Chapter(String titleKey, Page... pages) {
        this.titleKey = titleKey;
        this.pages = Lists.newArrayList(pages);
    }

    public Page get(int i) {
        if (i >= size()) return null;
        return pages.get(i);
    }

    public int size() {
        return pages.size();
    }
}
