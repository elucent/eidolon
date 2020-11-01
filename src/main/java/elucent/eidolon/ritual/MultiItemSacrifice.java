package elucent.eidolon.ritual;

import java.util.ArrayList;
import java.util.List;

public class MultiItemSacrifice {
    public Object main;
    public List<Object> items = new ArrayList<>();

    public MultiItemSacrifice(Object main, Object... items) {
        this.main = main;
        for (Object o : items) this.items.add(o);
    }
}
